package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateAppointmentRequest(
        @NotNull(message = "{appointment.create.bikeId.required}")
        UUID bikeId,

        @NotNull(message = "{appointment.create.storeLocationId.required}")
        UUID storeLocationId,

        @NotNull(message = "{appointment.create.scheduledAt.required}")
        Instant scheduledAt,

        String notes,

        @Valid
        List<RequestedServiceRequestItem> requestedServices
) {
    public record RequestedServiceRequestItem(
            @NotNull(message = "{appointment.create.requestedService.productId.required}")
            UUID productId
    ) {
    }
}

