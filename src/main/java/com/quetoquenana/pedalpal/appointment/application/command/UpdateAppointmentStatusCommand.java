package com.quetoquenana.pedalpal.appointment.application.command;

import java.util.UUID;

public record UpdateAppointmentStatusCommand(
        UUID id,
        String status
) {
}

