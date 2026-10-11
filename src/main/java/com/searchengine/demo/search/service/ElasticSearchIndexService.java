package com.searchengine.demo.search.service;

import com.searchengine.demo.model.Owner;
import com.searchengine.demo.model.Pet;
import com.searchengine.demo.model.Visit;
import com.searchengine.demo.search.dto.BulkIndexResult;

public interface ElasticSearchIndexService {
    void savePetDocument(Pet pet);

    void saveOwnerDocument(Owner savedOwner);

    void saveVisitDocument(Visit visit);

    BulkIndexResult indexAllFromPostgres();
}
