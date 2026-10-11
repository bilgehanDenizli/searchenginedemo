package com.searchengine.demo.search.controller;

import com.searchengine.demo.search.dto.BulkIndexResult;
import com.searchengine.demo.search.service.ElasticSearchIndexService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/search")
public class BulkIndexController {
	private final ElasticSearchIndexService elasticSearchIndexService;

	public BulkIndexController(ElasticSearchIndexService elasticSearchIndexService) {
		this.elasticSearchIndexService = elasticSearchIndexService;
	}

	@PostMapping("/index")
	public BulkIndexResult indexAllFromPostgres() {
		return elasticSearchIndexService.indexAllFromPostgres();
	}
}
