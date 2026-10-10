package com.searchengine.demo.search.repository;

import com.searchengine.demo.search.model.OwnerSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OwnerSearchRepository extends ElasticsearchRepository<OwnerSearchDocument, String> {
}
