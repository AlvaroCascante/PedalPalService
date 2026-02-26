package com.quetoquenana.pedalpal.appointment.application.command;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record UpdateAppointmentCommand(
        UUID id,
        UUID storeLocationId,
        Instant scheduledAt,
        String notes,

        List<CreateAppointmentCommand.ServiceCommandItem> requestedServices,
        UUID authenticatedUserId
) {
}

