package com.quetoquenana.pedalpal.serviceOrder.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum ServiceOrderStatus {
    CREATED("service.order.status.created"),
    WAITING_APPROVAL("service.order.status.waiting.approval"),
    APPROVED("service.order.status.approved"),
    IN_PROGRESS("service.order.status.in_progress"),
    WAITING_PARTS("service.order.status.waiting.parts"),
    READY_FOR_PICKUP("service.order.status.ready.for.pickup"),
    CANCELLED("service.order.status.cancelled"),
    COMPLETED("service.order.status.completed"),
    UNKNOWN("service.order.status.unknown");

    private final String key;

    ServiceOrderStatus(String key) {
        this.key = key;
    }

    public static ServiceOrderStatus from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
