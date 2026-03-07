package com.quetoquenana.pedalpal.appointment.application.result;

import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Result of a successful appointment status transition.
 */
public record ChangeAppointmentStatusResult(
        UUID appointmentId,
        AppointmentStatus fromStatus,
        AppointmentStatus toStatus,
        Instant changedAt,
        String serviceOrderNumber,
        BigDecimal deposit
) {
}

