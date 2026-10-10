package com.searchengine.demo.search.service;

import com.searchengine.demo.model.Owner;
import com.searchengine.demo.model.Pet;
import com.searchengine.demo.search.model.OwnerSearchDocument;
import com.searchengine.demo.search.repository.OwnerSearchRepository;
import com.searchengine.demo.search.repository.PetSearchRepository;
import com.searchengine.demo.search.model.PetSearchDocument;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchIndexServiceImpl implements ElasticSearchIndexService {

    private final PetSearchRepository petSearchRepository;
    private final OwnerSearchRepository ownerSearchRepository;

    public ElasticSearchIndexServiceImpl(PetSearchRepository petSearchElasticsearchRepository, OwnerSearchRepository ownerSearchRepository) {
        this.petSearchRepository = petSearchElasticsearchRepository;
        this.ownerSearchRepository = ownerSearchRepository;
    }

    @Override
    public void savePetDocument(Pet pet) {
        PetSearchDocument petSearchDocument =
                new PetSearchDocument(pet.getId().toString(), pet.getName(), pet.getSpecies(),
                        pet.getBreed(), pet.getBirthDate(), pet.getOwner().getFullName(), pet.getOwner().getId());
        petSearchRepository.save(petSearchDocument);
    }

    @Override
    public void deletePetDocument(Pet pet) {
        PetSearchDocument petSearchDocument =
                new PetSearchDocument(pet.getId().toString(), pet.getName(), pet.getSpecies(),
                        pet.getBreed(), pet.getBirthDate(), pet.getOwner().getFullName(), pet.getOwner().getId());
        petSearchRepository.delete(petSearchDocument);
    }

    @Override
    public void saveOwnerDocument(Owner owner) {
        OwnerSearchDocument ownerSearchDocument =
                new OwnerSearchDocument(owner.getId().toString(), owner.getFullName());
        ownerSearchRepository.save(ownerSearchDocument);
    }

    @Override
    public void deleteOwnerDocument(Owner owner) {
        OwnerSearchDocument ownerSearchDocument =
                new OwnerSearchDocument(owner.getId().toString(), owner.getFullName());
        ownerSearchRepository.delete(ownerSearchDocument);
    }
}
