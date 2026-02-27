package com.quetoquenana.pedalpal.appointment.application.command;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UpdateAppointmentCommand(
        UUID id,
        UUID storeLocationId,
        Instant scheduledAt,
        String notes,

        List<RequestedServiceCommand> requestedServices
) {
}

