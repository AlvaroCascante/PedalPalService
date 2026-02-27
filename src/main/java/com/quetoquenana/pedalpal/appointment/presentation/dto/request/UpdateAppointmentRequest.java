package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record UpdateAppointmentRequest(
        UUID storeLocationId,
        UUID authenticatedUserId,
        Instant scheduledAt,
        String notes,
        List<CreateAppointmentRequest.RequestedServiceRequestItem> requestedServices
) {
}

