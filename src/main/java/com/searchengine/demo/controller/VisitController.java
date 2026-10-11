package com.searchengine.demo.controller;

import com.searchengine.demo.dto.VisitRequest;
import com.searchengine.demo.dto.VisitResponse;
import com.searchengine.demo.dto.ActiveStatusRequest;
import com.searchengine.demo.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/visits")
public class VisitController {
	private final VisitService visitService;

	public VisitController(VisitService visitService) {
		this.visitService = visitService;
	}

	@GetMapping
	public List<VisitResponse> findAll() {
		return visitService.findAll();
	}

	@GetMapping("/{id}")
	public VisitResponse findById(@PathVariable Long id) {
		return visitService.findById(id);
	}

	@PostMapping
	public ResponseEntity<VisitResponse> create(@Valid @RequestBody VisitRequest request) {
		VisitResponse created = visitService.create(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(created.id()).toUri();
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	public VisitResponse update(@PathVariable Long id, @Valid @RequestBody VisitRequest request) {
		return visitService.update(id, request);
	}

	@PatchMapping("/{id}/active")
	public VisitResponse setActive(@PathVariable Long id, @Valid @RequestBody ActiveStatusRequest request) {
		return visitService.setActive(id, request.isActive());
	}
}
