package com.quetoquenana.pedalpal.appointment.application.command;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command to change an appointment status.
 */
public record ChangeAppointmentStatusCommand(
        UUID appointmentId,
        UUID customerId,
        String toStatus,
        String closureReason,
        UUID technicianId,
        String note,
        BigDecimal deposit
) {
}
