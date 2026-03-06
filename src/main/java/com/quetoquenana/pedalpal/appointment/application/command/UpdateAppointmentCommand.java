package com.quetoquenana.pedalpal.appointment.application.command;

import java.time.Instant;
import java.util.UUID;

public record UpdateAppointmentCommand(
        UUID id,
        UUID customerId,
        Instant scheduledAt,
        String notes
) {
}

