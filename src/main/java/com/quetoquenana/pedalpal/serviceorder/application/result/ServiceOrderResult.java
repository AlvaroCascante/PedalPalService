package com.quetoquenana.pedalpal.serviceorder.application.result;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;

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
        String orderNumber,
        BigDecimal totalPrice,
        String notes,
        List<ServiceOrderDetailResult> requestedServices
) {
}

