package com.quetoquenana.pedalpal.appointment.presentation.dto.response;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID bikeId,
        UUID storeLocationId,
        Instant scheduledAt,
        String status,
        String notes,
        Set<AppointmentServiceResponse> requestedServices
) {
}

