package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateAppointmentStatusRequest(
        @NotBlank(message = "{appointment.status.required}")
        String status
) {
}

