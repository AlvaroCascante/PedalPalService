package com.quetoquenana.pedalpal.serviceOrder.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum ServiceOrderDetailStatus {
    PENDING("service.order.item.status.pending"),
    IN_PROGRESS("service.order.item.status.in_progress"),
    COMPLETED("service.order.item.status.completed"),
    CANCELLED("service.order.item.status.cancelled"),
    REJECTED("service.order.item.status.rejected"),
    UNKNOWN("service.order.item.status.unknown");

    private final String key;

    ServiceOrderDetailStatus(String key) {
        this.key = key;
    }

    public static ServiceOrderDetailStatus from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
