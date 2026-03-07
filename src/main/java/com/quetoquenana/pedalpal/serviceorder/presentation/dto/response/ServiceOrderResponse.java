package com.quetoquenana.pedalpal.serviceorder.presentation.dto.response;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ServiceOrderResponse(
        UUID id,
        UUID appointmentId,
        UUID bikeId,
        Instant startedAt,
        Instant completedAt,
        ServiceOrderStatus status,
        String orderNumber,
        BigDecimal totalPrice,
        String notes,
        List<ServiceOrderDetailResponse> requestedServices
) {
}

