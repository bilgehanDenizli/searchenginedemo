package com.searchengine.demo.search.service;

import com.searchengine.demo.model.Owner;
import com.searchengine.demo.model.Pet;

public interface ElasticSearchIndexService {
    void savePetDocument(Pet pet);

    void deletePetDocument(Pet pet);

    void saveOwnerDocument(Owner savedOwner);

    void deleteOwnerDocument(Owner owner);
}
