package com.quetoquenana.pedalpal.serviceorder.presentation.dto.response;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetailStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ServiceOrderDetailResponse(
        UUID id,
        UUID productId,
        UUID technicianId,
        String productName,
        BigDecimal price,
        ServiceOrderDetailStatus status,
        Instant startedAt,
        Instant completedAt,
        String notes
) {
}

