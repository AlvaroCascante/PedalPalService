package com.quetoquenana.pedalpal.appointment.application.result;

import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;

import java.time.Instant;
import java.util.UUID;

public record AppointmentListItemResult(
        UUID id,
        UUID bikeId,
        UUID storeLocationId,
        Instant scheduledAt,
        AppointmentStatus status
) {
}

