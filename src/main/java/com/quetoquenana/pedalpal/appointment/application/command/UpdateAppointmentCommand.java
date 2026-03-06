package com.quetoquenana.pedalpal.appointment.application.command;

import java.time.Instant;
import java.util.UUID;

public record UpdateAppointmentCommand(
        UUID id,
        Instant scheduledAt,
        String notes
) {
}

