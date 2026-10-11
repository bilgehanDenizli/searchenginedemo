package com.searchengine.demo.dto;

import com.searchengine.demo.model.VisitStatus;

import java.time.LocalDateTime;

public record VisitResponse(
		Long id,
		Long petId,
		String petName,
		LocalDateTime scheduledAt,
		String reason,
		String notes,
		VisitStatus status,
		boolean isActive) {
}
