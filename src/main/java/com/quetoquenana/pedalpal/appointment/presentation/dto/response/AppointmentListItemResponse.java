package com.quetoquenana.pedalpal.appointment.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

public record AppointmentListItemResponse(
        UUID id,
        UUID bikeId,
        UUID storeLocationId,
        Instant scheduledAt,
        String status
) {
}

