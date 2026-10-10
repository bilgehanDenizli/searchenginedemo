package com.searchengine.demo.service;

import com.searchengine.demo.dto.PetRequest;
import com.searchengine.demo.dto.PetResponse;
import com.searchengine.demo.model.Owner;
import com.searchengine.demo.model.Pet;
import com.searchengine.demo.repository.OwnerRepository;
import com.searchengine.demo.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PetService {
	private final PetRepository petRepository;
	private final OwnerRepository ownerRepository;

	public PetService(PetRepository petRepository, OwnerRepository ownerRepository) {
		this.petRepository = petRepository;
		this.ownerRepository = ownerRepository;
	}

	public List<PetResponse> findAll() {
		return petRepository.findAll().stream().map(this::toResponse).toList();
	}

	public PetResponse findById(Long id) {
		return toResponse(requirePet(id));
	}

	public PetResponse create(PetRequest request) {
		Owner owner = requireOwner(request.ownerId());
		Pet pet = new Pet(request.name().trim(), request.species(), normalize(request.breed()), request.birthDate(), owner);
		return toResponse(petRepository.save(pet));
	}

	public PetResponse update(Long id, PetRequest request) {
		Pet pet = requirePet(id);
		pet.setName(request.name().trim());
		pet.setSpecies(request.species());
		pet.setBreed(normalize(request.breed()));
		pet.setBirthDate(request.birthDate());
		pet.setOwner(requireOwner(request.ownerId()));
		return toResponse(petRepository.save(pet));
	}

	public void delete(Long id) {
		petRepository.delete(requirePet(id));
	}

	private Pet requirePet(Long id) {
		return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet", id));
	}

	private Owner requireOwner(Long id) {
		return ownerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", id));
	}

	private String normalize(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}

	private PetResponse toResponse(Pet pet) {
		Owner owner = pet.getOwner();
		return new PetResponse(pet.getId(), pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getBirthDate(),
				owner.getId(), owner.getFirstName() + " " + owner.getLastName());
	}
}
