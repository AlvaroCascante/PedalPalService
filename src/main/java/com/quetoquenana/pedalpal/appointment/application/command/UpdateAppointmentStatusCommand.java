package com.quetoquenana.pedalpal.appointment.application.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateAppointmentStatusCommand(
        UUID id,
        String status,
        UUID authenticatedUserId
) {
}

