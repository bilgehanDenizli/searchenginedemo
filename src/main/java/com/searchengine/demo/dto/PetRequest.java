package com.searchengine.demo.dto;

import com.searchengine.demo.model.Species;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PetRequest(
		@NotBlank @Size(max = 100) String name,
		@NotNull Species species,
		@Size(max = 100) String breed,
		@PastOrPresent LocalDate birthDate,
		@NotNull Long ownerId) {
}
