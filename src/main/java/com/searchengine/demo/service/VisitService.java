package com.searchengine.demo.service;

import com.searchengine.demo.dto.VisitRequest;
import com.searchengine.demo.dto.VisitResponse;
import com.searchengine.demo.fault.exception.ResourceNotFoundException;
import com.searchengine.demo.model.Pet;
import com.searchengine.demo.model.Visit;
import com.searchengine.demo.model.VisitStatus;
import com.searchengine.demo.repository.PetRepository;
import com.searchengine.demo.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VisitService {
	private final VisitRepository visitRepository;
	private final PetRepository petRepository;

	public VisitService(VisitRepository visitRepository, PetRepository petRepository) {
		this.visitRepository = visitRepository;
		this.petRepository = petRepository;
	}

	public List<VisitResponse> findAll() {
		return visitRepository.findAll().stream().map(this::toResponse).toList();
	}

	public VisitResponse findById(Long id) {
		return toResponse(requireVisit(id));
	}

	public VisitResponse create(VisitRequest request) {
		Pet pet = requirePet(request.petId());
		Visit visit = new Visit(pet, request.scheduledAt(), request.reason().trim(), normalize(request.notes()),
				request.status() == null ? VisitStatus.SCHEDULED : request.status());
		return toResponse(visitRepository.save(visit));
	}

	public VisitResponse update(Long id, VisitRequest request) {
		Visit visit = requireVisit(id);
		visit.setPet(requirePet(request.petId()));
		visit.setScheduledAt(request.scheduledAt());
		visit.setReason(request.reason().trim());
		visit.setNotes(normalize(request.notes()));
		if (request.status() != null) {
			visit.setStatus(request.status());
		}
		return toResponse(visitRepository.save(visit));
	}

	public void delete(Long id) {
		visitRepository.delete(requireVisit(id));
	}

	private Visit requireVisit(Long id) {
		return visitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Visit", id));
	}

	private Pet requirePet(Long id) {
		return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet", id));
	}

	private String normalize(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}

	private VisitResponse toResponse(Visit visit) {
		return new VisitResponse(visit.getId(), visit.getPet().getId(), visit.getPet().getName(), visit.getScheduledAt(),
				visit.getReason(), visit.getNotes(), visit.getStatus());
	}
}
