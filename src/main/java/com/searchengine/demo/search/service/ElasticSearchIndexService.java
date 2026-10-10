package com.searchengine.demo.search.service;

import com.searchengine.demo.model.Pet;

public interface ElasticSearchIndexService {
    void createPetDocument(Pet pet);
}
