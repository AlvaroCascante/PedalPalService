package com.quetoquenana.pedalpal.appointment.application.result;

import java.math.BigDecimal;
import java.util.UUID;

public record AppointmentServiceResult(
        UUID id,
        UUID productId,
        String productNameSnapshot,
        BigDecimal priceSnapshot
) {
}

