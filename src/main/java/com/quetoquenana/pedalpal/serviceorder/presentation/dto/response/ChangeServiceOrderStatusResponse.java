package com.quetoquenana.pedalpal.serviceorder.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * API response for service order status transitions.
 */
public record ChangeServiceOrderStatusResponse(
        UUID serviceOrderId,
        String orderNumber,
        String fromStatus,
        String toStatus,
        Instant startedAt,
        Instant completedAt
) {
}

