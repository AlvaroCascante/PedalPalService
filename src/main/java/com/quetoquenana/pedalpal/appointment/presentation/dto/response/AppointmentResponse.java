package com.quetoquenana.pedalpal.appointment.presentation.dto.response;

import java.math.BigDecimal;
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
        BigDecimal deposit,
        Set<AppointmentServiceResponse> requestedServices
) {
}

