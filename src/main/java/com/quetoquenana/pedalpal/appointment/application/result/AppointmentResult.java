package com.quetoquenana.pedalpal.appointment.application.result;

import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AppointmentResult(
        UUID id,
        UUID bikeId,
        UUID customerId,
        UUID storeLocationId,
        Instant scheduledAt,
        AppointmentStatus status,
        String notes,
        BigDecimal deposit,
        List<AppointmentServiceResult> requestedServices
) {
}

