package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RequestedServiceItemRequest(
        @NotNull(message = "{appointment.service.serviceId.required}")
        UUID serviceId,

        @NotNull(message = "{appointment.service.serviceType.required}")
        String serviceType // Indicate if is a Package or a Single Service
) {
}
