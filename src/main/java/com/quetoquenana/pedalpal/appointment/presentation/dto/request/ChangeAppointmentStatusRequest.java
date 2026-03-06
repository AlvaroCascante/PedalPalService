package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * API request to transition an appointment to a new status.
 */
public record ChangeAppointmentStatusRequest(
        @NotBlank(message = "{appointment.status.required}")
        String toStatus,
        UUID customerId,
        String closureReason,
        UUID technicianId,
        String note
) {
}

