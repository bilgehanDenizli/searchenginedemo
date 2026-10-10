package com.searchengine.demo.repository;

import com.searchengine.demo.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
	Optional<Owner> findByEmail(String email);
}
