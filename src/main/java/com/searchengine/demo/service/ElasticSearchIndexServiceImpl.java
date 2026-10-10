package com.searchengine.demo.service;

import com.searchengine.demo.model.Pet;
import com.searchengine.demo.search.PetSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public class ElasticSearchIndexServiceImpl implements ElasticSearchIndexService {
    private final ElasticsearchRepository<PetSearchDocument, String> petSearchElasticsearchRepository;

    public ElasticSearchIndexServiceImpl(ElasticsearchRepository<PetSearchDocument, String> petSearchElasticsearchRepository) {
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
