package com.searchengine.demo.repository;

import com.searchengine.demo.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
	List<Visit> findAllByActiveTrueOrderByIdAsc();
}
