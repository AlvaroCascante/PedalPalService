package com.quetoquenana.pedalpal.appointment.application.command;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateAppointmentCommand(
        UUID bikeId,
        UUID storeLocationId,
        Instant scheduledAt,
        String notes,
        List<ServiceCommandItem> requestedServices,
        UUID authenticatedUserId
) {

    @Builder
    public record ServiceCommandItem(
            UUID productId
    ) {
    }
}

