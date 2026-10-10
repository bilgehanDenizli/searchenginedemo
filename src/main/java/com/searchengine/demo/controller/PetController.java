package com.searchengine.demo.controller;

import com.searchengine.demo.dto.PetRequest;
import com.searchengine.demo.dto.PetResponse;
import com.searchengine.demo.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
	private final PetService petService;

	public PetController(PetService petService) {
		this.petService = petService;
	}

	@GetMapping
	public List<PetResponse> findAll() {
		return petService.findAll();
	}

	@GetMapping("/{id}")
	public PetResponse findById(@PathVariable Long id) {
		return petService.findById(id);
	}

	@PostMapping
	public ResponseEntity<PetResponse> create(@Valid @RequestBody PetRequest request) {
		PetResponse created = petService.create(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(created.id()).toUri();
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	public PetResponse update(@PathVariable Long id, @Valid @RequestBody PetRequest request) {
		return petService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		petService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
