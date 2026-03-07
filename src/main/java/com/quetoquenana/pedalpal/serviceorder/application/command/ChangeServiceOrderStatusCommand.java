package com.quetoquenana.pedalpal.serviceorder.application.command;

import java.util.UUID;

/**
 * Command to change a service order status.
 */
public record ChangeServiceOrderStatusCommand(
        UUID serviceOrderId,
        String toStatus,
        UUID technicianId,
        String note
) {
}
