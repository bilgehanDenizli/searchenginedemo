package com.searchengine.demo.search.controller;

import com.searchengine.demo.model.Species;
import com.searchengine.demo.search.dto.SearchPage;
import com.searchengine.demo.search.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {
	private final SearchService searchService;

	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

	@GetMapping
	public SearchPage searchAll(
			@RequestParam String q,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		return searchService.searchAll(q, page, size);
	}

	@GetMapping("/pets")
	public SearchPage searchPets(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) Species species,
			@RequestParam(required = false) Long ownerId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		return searchService.searchPets(q, species, ownerId, page, size);
	}

	@GetMapping("/owners")
	public SearchPage searchOwners(
			@RequestParam String q,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		return searchService.searchOwners(q, page, size);
	}
}
