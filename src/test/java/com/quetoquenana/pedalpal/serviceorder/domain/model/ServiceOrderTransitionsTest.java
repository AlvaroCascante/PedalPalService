package com.quetoquenana.pedalpal.serviceorder.domain.model;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ServiceOrderTransitionsTest {

    @Test
    void shouldMatchExpectedTransitionMatrix() {
        Map<ServiceOrderStatus, Set<ServiceOrderStatus>> expected = expectedRules();

        for (ServiceOrderStatus from : ServiceOrderStatus.values()) {
            for (ServiceOrderStatus to : ServiceOrderStatus.values()) {
                boolean allowed = expected.getOrDefault(from, Set.of()).contains(to);
                assertEquals(allowed, ServiceOrderTransitions.canTransition(from, to));
            }
        }
    }

    @Test
    void shouldReturnAllowedTargetsForEachSourceStatus() {
        Map<ServiceOrderStatus, Set<ServiceOrderStatus>> expected = expectedRules();

        for (ServiceOrderStatus from : ServiceOrderStatus.values()) {
            assertEquals(expected.getOrDefault(from, Set.of()), ServiceOrderTransitions.allowedFrom(from));
        }
    }

    @Test
    void shouldReturnFalseWhenTransitionContainsNullStatus() {
        assertFalse(ServiceOrderTransitions.canTransition(null, ServiceOrderStatus.CREATED));
        assertFalse(ServiceOrderTransitions.canTransition(ServiceOrderStatus.CREATED, null));
    }

    @Test
    void shouldReturnEmptySetWhenSourceStatusIsNull() {
        assertEquals(Set.of(), ServiceOrderTransitions.allowedFrom(null));
    }

    private Map<ServiceOrderStatus, Set<ServiceOrderStatus>> expectedRules() {
        Map<ServiceOrderStatus, Set<ServiceOrderStatus>> rules = new EnumMap<>(ServiceOrderStatus.class);
        rules.put(ServiceOrderStatus.CREATED, EnumSet.of(
                ServiceOrderStatus.IN_PROGRESS,
                ServiceOrderStatus.AWAITING_PARTS,
                ServiceOrderStatus.CANCELED
        ));
        rules.put(ServiceOrderStatus.IN_PROGRESS, EnumSet.of(
                ServiceOrderStatus.AWAITING_PARTS,
                ServiceOrderStatus.COMPLETED,
                ServiceOrderStatus.CANCELED
        ));
        rules.put(ServiceOrderStatus.AWAITING_PARTS, EnumSet.of(
                ServiceOrderStatus.IN_PROGRESS,
                ServiceOrderStatus.CANCELED
        ));
        rules.put(ServiceOrderStatus.COMPLETED, EnumSet.noneOf(ServiceOrderStatus.class));
        rules.put(ServiceOrderStatus.CANCELED, EnumSet.noneOf(ServiceOrderStatus.class));
        rules.put(ServiceOrderStatus.UNKNOWN, EnumSet.noneOf(ServiceOrderStatus.class));
        return rules;
    }
}

