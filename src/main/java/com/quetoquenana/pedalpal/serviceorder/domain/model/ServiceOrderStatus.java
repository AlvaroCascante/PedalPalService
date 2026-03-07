package com.quetoquenana.pedalpal.serviceorder.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum ServiceOrderStatus {
    CREATED("service.order.status.created"),
    IN_PROGRESS("service.order.status.in_progress"),
    AWAITING_PARTS("service.order.status.awaiting_parts"),
    COMPLETED("service.order.status.completed"),
    CANCELED("service.order.status.canceled"),
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
