package com.searchengine.demo.service;

import com.searchengine.demo.dto.OwnerRequest;
import com.searchengine.demo.dto.OwnerResponse;
import com.searchengine.demo.model.Owner;
import com.searchengine.demo.repository.OwnerRepository;
import com.searchengine.demo.search.service.ElasticSearchIndexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class OwnerService {
	private final OwnerRepository ownerRepository;
	private final ElasticSearchIndexService elasticSearchIndexService;

	public OwnerService(OwnerRepository ownerRepository, ElasticSearchIndexService elasticSearchIndexService) {
		this.ownerRepository = ownerRepository;
        this.elasticSearchIndexService = elasticSearchIndexService;
    }

	public List<OwnerResponse> findAll() {
		return ownerRepository.findAll().stream().map(this::toResponse).toList();
	}

	public OwnerResponse findById(Long id) {
		return toResponse(requireOwner(id));
	}

	public OwnerResponse create(OwnerRequest request) {
		String email = normalizeEmail(request.email());
		ensureEmailAvailable(email, null);
		Owner owner = new Owner(request.firstName().trim(), request.lastName().trim(), email, normalizePhone(request.phone()));
		Owner savedOwner = ownerRepository.save(owner);
		elasticSearchIndexService.saveOwnerDocument(savedOwner);
		return toResponse(savedOwner);
	}

	public OwnerResponse update(Long id, OwnerRequest request) {
		Owner owner = requireOwner(id);
		String email = normalizeEmail(request.email());
		ensureEmailAvailable(email, id);
		owner.setFirstName(request.firstName().trim());
		owner.setLastName(request.lastName().trim());
		owner.setEmail(email);
		owner.setPhone(normalizePhone(request.phone()));
		Owner savedOwner = ownerRepository.save(owner);
		elasticSearchIndexService.saveOwnerDocument(savedOwner);
		return toResponse(savedOwner);
	}

	public void delete(Long id) {
		Owner owner = ownerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", id));
		ownerRepository.delete(owner);
		elasticSearchIndexService.deleteOwnerDocument(owner);
	}

	private Owner requireOwner(Long id) {
		return ownerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", id));
	}

	private void ensureEmailAvailable(String email, Long currentOwnerId) {
		ownerRepository.findByEmail(email)
				.filter(existing -> !existing.getId().equals(currentOwnerId))
				.ifPresent(existing -> {
					throw new ConflictException("An owner with that email already exists");
				});
	}

	private String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}

	private String normalizePhone(String phone) {
		return phone == null || phone.isBlank() ? null : phone.trim();
	}

	private OwnerResponse toResponse(Owner owner) {
		return new OwnerResponse(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail(), owner.getPhone());
	}
}
