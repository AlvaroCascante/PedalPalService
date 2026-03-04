package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateAppointmentRequest(
        @NotNull(message = "{appointment.bikeId.required}")
        UUID bikeId,

        @NotNull(message = "{appointment.storeLocationId.required}")
        UUID storeLocationId,

        @NotNull(message = "{appointment.scheduledAt.required}")
        Instant scheduledAt,

        String notes,

        @Valid
        @NotNull(message = "{appointment.requestedServices.required}")
        List<RequestedServiceItemRequest> requestedServices
) {
}

