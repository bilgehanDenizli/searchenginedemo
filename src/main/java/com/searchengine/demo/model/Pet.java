package com.searchengine.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pets")
public class Pet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private Species species;

	@Column(length = 100)
	private String breed;

	private LocalDate birthDate;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	private Owner owner;

	@Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
	private boolean active = true;

	protected Pet() {
	}

	public Pet(String name, Species species, String breed, LocalDate birthDate, Owner owner) {
		this.name = name;
		this.species = species;
		this.breed = breed;
		this.birthDate = birthDate;
		this.owner = owner;
	}
}
