package com.searchengine.demo.dto;

import com.searchengine.demo.model.Species;

import java.time.LocalDate;

public record PetResponse(
		Long id,
		String name,
		Species species,
		String breed,
		LocalDate birthDate,
		Long ownerId,
		String ownerName,
		boolean isActive) {
}
