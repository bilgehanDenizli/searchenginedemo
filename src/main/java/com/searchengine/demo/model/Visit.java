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

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "visits")
public class Visit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pet_id", nullable = false)
	private Pet pet;

	@Column(nullable = false)
	private LocalDateTime scheduledAt;

	@Column(nullable = false, length = 500)
	private String reason;

	@Column(length = 2000)
	private String notes;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private VisitStatus status;

	@Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
	private boolean active = true;

	protected Visit() {
	}

	public Visit(Pet pet, LocalDateTime scheduledAt, String reason, String notes, VisitStatus status) {
		this.pet = pet;
		this.scheduledAt = scheduledAt;
		this.reason = reason;
		this.notes = notes;
		this.status = status;
	}
}
