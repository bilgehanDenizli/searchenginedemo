package com.searchengine.demo.dto;

import com.searchengine.demo.model.VisitStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record VisitRequest(
		@NotNull Long petId,
		@NotNull LocalDateTime scheduledAt,
		@NotBlank @Size(max = 500) String reason,
		@Size(max = 2000) String notes,
		VisitStatus status) {
}
