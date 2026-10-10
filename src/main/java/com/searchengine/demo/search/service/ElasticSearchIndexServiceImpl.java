package com.searchengine.demo.search.service;

import com.searchengine.demo.model.Pet;
import com.searchengine.demo.search.repository.PetSearchRepository;
import com.searchengine.demo.search.model.PetSearchDocument;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchIndexServiceImpl implements ElasticSearchIndexService {

    private final PetSearchRepository petSearchElasticsearchRepository;

    public ElasticSearchIndexServiceImpl(PetSearchRepository petSearchElasticsearchRepository) {
        this.petSearchElasticsearchRepository = petSearchElasticsearchRepository;
    }

    @Override
    public void createPetDocument(Pet pet) {
        PetSearchDocument petSearchDocument =
                new PetSearchDocument(pet.getId().toString(), pet.getName(), pet.getSpecies(),
                        pet.getBreed(), pet.getBirthDate(), pet.getOwner().getFullName(), pet.getOwner().getId());
        petSearchElasticsearchRepository.save(petSearchDocument);
    }
}
