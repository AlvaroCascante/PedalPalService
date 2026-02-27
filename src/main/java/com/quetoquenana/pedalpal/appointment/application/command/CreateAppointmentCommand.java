package com.quetoquenana.pedalpal.appointment.application.command;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateAppointmentCommand(
        UUID bikeId,
        UUID authenticatedUserId,
        UUID storeLocationId,
        Instant scheduledAt,
        String notes,
        List<RequestedServiceCommand> requestedServices
) {

    @Builder
    public record RequestedServiceCommand(
            UUID productId,
            String productNameSnapshot,
            BigDecimal priceSnapshot
    ) {
    }
}

