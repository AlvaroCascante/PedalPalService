package com.quetoquenana.pedalpal.appointment.application.command;

import com.quetoquenana.pedalpal.appointment.domain.model.ServiceType;

import java.util.UUID;

public record RequestedServiceCommand(
        UUID serviceId,
        ServiceType serviceType
) {
}

