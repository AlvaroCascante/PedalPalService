package com.quetoquenana.pedalpal.serviceOrder.application.result;

import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ServiceOrderResult(
        UUID id,
        UUID appointmentId,
        UUID bikeId,
        Instant startedAt,
        Instant completedAt,
        ServiceOrderStatus status,
        BigDecimal totalPrice,
        List<ServiceOrderDetailResult> requestedServices
) {
}

