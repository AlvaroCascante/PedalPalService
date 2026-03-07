package com.quetoquenana.pedalpal.serviceorder.application.result;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Result of a successful service order status transition.
 */
public record ChangeServiceOrderStatusResult(
        UUID serviceOrderId,
        String orderNumber,
        ServiceOrderStatus fromStatus,
        ServiceOrderStatus toStatus,
        Instant startedAt,
        Instant completedAt
) {
}
