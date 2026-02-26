package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record UpdateAppointmentRequest(
        @NotNull(message = "{appointment.update.storeLocationId.required}")
        UUID storeLocationId,

        @NotNull(message = "{appointment.update.scheduledAt.required}")
        Instant scheduledAt,

        String notes,

        @NotNull(message = "{appointment.update.version.required}")
        Long version,

        @Valid
        List<CreateAppointmentRequest.RequestedServiceRequestItem> requestedServices
) {
}

