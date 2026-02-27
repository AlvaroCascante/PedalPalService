package com.quetoquenana.pedalpal.appointment.presentation.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ConfirmAppointmentResponse(
        UUID id,
        UUID bikeId,
        UUID storeLocationId,
        Instant scheduledAt,
        String status,
        String notes,
        String serviceOrderNumber,
        List<AppointmentServiceResponse> requestedServices
) {
}

