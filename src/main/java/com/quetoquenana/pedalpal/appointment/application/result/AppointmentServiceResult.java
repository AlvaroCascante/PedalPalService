package com.quetoquenana.pedalpal.appointment.application.result;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record AppointmentServiceResult(
        UUID id,
        UUID productId,
        String productNameSnapshot,
        BigDecimal priceSnapshot
) {
}

