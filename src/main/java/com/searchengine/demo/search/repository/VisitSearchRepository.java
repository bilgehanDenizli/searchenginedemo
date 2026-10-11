package com.searchengine.demo.search.repository;

import com.searchengine.demo.search.model.VisitSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VisitSearchRepository extends ElasticsearchRepository<VisitSearchDocument, String> {
}
