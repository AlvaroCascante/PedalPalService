package com.quetoquenana.pedalpal.appointment.application.result;

import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record AppointmentResult(
        UUID id,
        UUID bikeId,
        UUID storeLocationId,
        Instant scheduledAt,
        AppointmentStatus status,
        String notes,
        List<AppointmentServiceResult> requestedServices
) {
}

