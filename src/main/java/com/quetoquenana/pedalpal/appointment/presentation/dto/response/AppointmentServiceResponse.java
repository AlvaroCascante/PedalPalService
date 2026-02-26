package com.quetoquenana.pedalpal.appointment.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record AppointmentServiceResponse(
        UUID id,
        UUID productId,
        String productNameSnapshot,
        BigDecimal priceSnapshot
) {
}

