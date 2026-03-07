package com.quetoquenana.pedalpal.serviceorder.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * API request to transition a service order to a new status.
 */
public record ChangeServiceOrderStatusRequest(
        @NotBlank(message = "{service.order.status.required}")
        String toStatus,
        UUID technicianId,
        String note
) {
}

