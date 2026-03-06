package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UpdateAppointmentRequest(
        UUID authenticatedUserId,
        Instant scheduledAt,
        String notes
) {
}

