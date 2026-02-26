package com.quetoquenana.pedalpal.appointment.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateAppointmentStatusRequest(
        @NotNull(message = "{appointment.update.status.required}")
        String status
) {
}

