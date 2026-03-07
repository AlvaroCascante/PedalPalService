package com.quetoquenana.pedalpal.appointment.application.command;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateAppointmentCommand(
        UUID bikeId,
        UUID customerId,
        UUID storeLocationId,
        Instant scheduledAt,
        String notes,
        BigDecimal deposit,
        List<RequestedServiceCommand> requestedServices
) {
}
