package com.searchengine.demo.repository;

import com.searchengine.demo.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
