package com.searchengine.demo.search.service;

import com.searchengine.demo.model.Owner;
import com.searchengine.demo.model.Pet;
import com.searchengine.demo.model.Visit;
import com.searchengine.demo.repository.OwnerRepository;
import com.searchengine.demo.repository.PetRepository;
import com.searchengine.demo.repository.VisitRepository;
import com.searchengine.demo.search.dto.BulkIndexResult;
import com.searchengine.demo.search.model.OwnerSearchDocument;
import com.searchengine.demo.search.model.VisitSearchDocument;
import com.searchengine.demo.search.repository.OwnerSearchRepository;
import com.searchengine.demo.search.repository.PetSearchRepository;
import com.searchengine.demo.search.model.PetSearchDocument;
import com.searchengine.demo.search.repository.VisitSearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ElasticSearchIndexServiceImpl implements ElasticSearchIndexService {

    private final PetSearchRepository petSearchRepository;
    private final OwnerSearchRepository ownerSearchRepository;
    private final VisitSearchRepository visitSearchRepository;
    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final VisitRepository visitRepository;

    public ElasticSearchIndexServiceImpl(PetSearchRepository petSearchRepository,
                                         OwnerSearchRepository ownerSearchRepository,
                                         VisitSearchRepository visitSearchRepository,
                                         OwnerRepository ownerRepository,
                                         PetRepository petRepository,
                                         VisitRepository visitRepository) {
        this.petSearchRepository = petSearchRepository;
        this.ownerSearchRepository = ownerSearchRepository;
        this.visitSearchRepository = visitSearchRepository;
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    public void savePetDocument(Pet pet) {
        PetSearchDocument petSearchDocument =
                new PetSearchDocument(pet.getId().toString(), pet.getName(), pet.getSpecies(),
                        pet.getBreed(), pet.getBirthDate(), pet.getOwner().getFullName(), pet.getOwner().getId(),
                        pet.isActive());
        petSearchRepository.save(petSearchDocument);
    }

    @Override
    public void saveOwnerDocument(Owner owner) {
        OwnerSearchDocument ownerSearchDocument =
                new OwnerSearchDocument(owner.getId().toString(), owner.getFullName(), owner.isActive());
        ownerSearchRepository.save(ownerSearchDocument);
    }

    @Override
    public void saveVisitDocument(Visit visit) {
        visitSearchRepository.save(toVisitDocument(visit));
    }

    @Override
    @Transactional(readOnly = true)
    public BulkIndexResult indexAllFromPostgres() {
        List<Owner> owners = ownerRepository.findAll();
        List<Pet> pets = petRepository.findAll();
        List<Visit> visits = visitRepository.findAll();

        ownerSearchRepository.saveAll(owners.stream().map(ElasticSearchIndexServiceImpl::toOwnerDocument).toList());
        petSearchRepository.saveAll(pets.stream().map(ElasticSearchIndexServiceImpl::toPetDocument).toList());
        visitSearchRepository.saveAll(visits.stream().map(ElasticSearchIndexServiceImpl::toVisitDocument).toList());

        return new BulkIndexResult(owners.size(), pets.size(), visits.size());
    }

    private static OwnerSearchDocument toOwnerDocument(Owner owner) {
        return new OwnerSearchDocument(owner.getId().toString(), owner.getFullName(), owner.isActive());
    }

    private static PetSearchDocument toPetDocument(Pet pet) {
        return new PetSearchDocument(pet.getId().toString(), pet.getName(), pet.getSpecies(), pet.getBreed(),
                pet.getBirthDate(), pet.getOwner().getFullName(), pet.getOwner().getId(), pet.isActive());
    }

    private static VisitSearchDocument toVisitDocument(Visit visit) {
        return new VisitSearchDocument(visit.getId().toString(), visit.getPet().getOwner().getFullName(),
                visit.getPet().getName(), visit.getReason(), visit.getNotes(),
                visit.getScheduledAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                visit.getStatus().name(), visit.isActive());
    }
}
