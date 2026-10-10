package com.searchengine.demo.search.repository;

import com.searchengine.demo.search.model.PetSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PetSearchRepository extends ElasticsearchRepository<PetSearchDocument, String> {
}
