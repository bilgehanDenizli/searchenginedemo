package com.searchengine.demo.repository;

import com.searchengine.demo.search.PetSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PetSearchRepository extends ElasticsearchRepository<PetSearchDocument, String> {
}
