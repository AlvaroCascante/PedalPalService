package com.quetoquenana.pedalpal.serviceorder.application.result;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetailStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ServiceOrderDetailResult(
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

