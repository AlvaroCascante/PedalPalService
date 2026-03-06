package com.quetoquenana.pedalpal.serviceOrder.application.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Year;

/**
 * Generates service order numbers in format PREFIX-YYYY-000001.
 */
@Component
public class ServiceOrderNumberGenerator {

    private final String prefix;

    public ServiceOrderNumberGenerator(@Value("${app.service.order.number.prefix}") String prefix) {
        this.prefix = prefix;
    }

    public String generate(long sequence, Clock clock) {
        int year = Year.now(clock).getValue();
        return "%s-%d-%06d".formatted(prefix, year, sequence);
    }
}

