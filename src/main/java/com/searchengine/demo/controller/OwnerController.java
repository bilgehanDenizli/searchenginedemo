package com.searchengine.demo.controller;

import com.searchengine.demo.dto.OwnerRequest;
import com.searchengine.demo.dto.OwnerResponse;
import com.searchengine.demo.service.OwnerService;
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
@RequestMapping("/api/owners")
public class OwnerController {
	private final OwnerService ownerService;

	public OwnerController(OwnerService ownerService) {
		this.ownerService = ownerService;
	}

	@GetMapping
	public List<OwnerResponse> findAll() {
		return ownerService.findAll();
	}

	@GetMapping("/{id}")
	public OwnerResponse findById(@PathVariable Long id) {
		return ownerService.findById(id);
	}

	@PostMapping
	public ResponseEntity<OwnerResponse> create(@Valid @RequestBody OwnerRequest request) {
		OwnerResponse created = ownerService.create(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(created.id()).toUri();
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	public OwnerResponse update(@PathVariable Long id, @Valid @RequestBody OwnerRequest request) {
		return ownerService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		ownerService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
