package com.searchengine.demo.search.service;

import com.searchengine.demo.model.Owner;
import com.searchengine.demo.model.Pet;
import com.searchengine.demo.search.model.OwnerSearchDocument;
import com.searchengine.demo.search.repository.OwnerSearchRepository;
import com.searchengine.demo.search.repository.PetSearchRepository;
import com.searchengine.demo.search.model.PetSearchDocument;
import com.searchengine.demo.service.OwnerService;
import com.searchengine.demo.service.PetService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchIndexServiceImpl implements ElasticSearchIndexService {

    private final PetSearchRepository petSearchRepository;
    private final OwnerSearchRepository ownerSearchRepository;
    private final PetService petService;
    private final OwnerService ownerService;

    public ElasticSearchIndexServiceImpl(PetSearchRepository petSearchElasticsearchRepository, OwnerSearchRepository ownerSearchRepository, PetService petService, OwnerService ownerService) {
        this.petSearchRepository = petSearchElasticsearchRepository;
        this.ownerSearchRepository = ownerSearchRepository;
        this.petService = petService;
        this.ownerService = ownerService;
    }

    @Override
    @Async
    public void savePetDocument(Pet pet) {
        PetSearchDocument petSearchDocument =
                new PetSearchDocument(pet.getId().toString(), pet.getName(), pet.getSpecies(),
                        pet.getBreed(), pet.getBirthDate(), pet.getOwner().getFullName(), pet.getOwner().getId(),
                        pet.isActive());
        petSearchRepository.save(petSearchDocument);
    }

    @Override
    @Async
    public void saveOwnerDocument(Owner owner) {
        OwnerSearchDocument ownerSearchDocument =
                new OwnerSearchDocument(owner.getId().toString(), owner.getFullName(), owner.isActive());
        ownerSearchRepository.save(ownerSearchDocument);
    }
}
