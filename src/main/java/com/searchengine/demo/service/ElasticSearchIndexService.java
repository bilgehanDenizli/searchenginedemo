package com.searchengine.demo.service;

import com.searchengine.demo.model.Pet;

public interface ElasticSearchIndexService {
    void createPetDocument(Pet pet);
}
