package com.quetoquenana.pedalpal.appointment.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * API response for appointment status transitions.
 */
public record ChangeAppointmentStatusResponse(
        UUID appointmentId,
        String fromStatus,
        String toStatus,
        Instant changedAt,
        String serviceOrderNumber
) {
}

