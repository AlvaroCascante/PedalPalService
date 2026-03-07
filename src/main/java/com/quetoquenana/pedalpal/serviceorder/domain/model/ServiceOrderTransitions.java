package com.quetoquenana.pedalpal.serviceorder.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.StateMachine;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines allowed service order status transitions.
 */
public final class ServiceOrderTransitions {

    private static final StateMachine<ServiceOrderStatus> MACHINE;

    static {
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
        MACHINE = new StateMachine<>(Map.copyOf(rules));
    }

    private ServiceOrderTransitions() {
    }

    /**
     * Checks whether transitioning from one status to another is allowed.
     */
    public static boolean canTransition(ServiceOrderStatus from, ServiceOrderStatus to) {
        if (from == null || to == null) {
            return false;
        }
        return MACHINE.canTransition(from, to);
    }

    /**
     * Returns the allowed target statuses for a given source status.
     */
    public static Set<ServiceOrderStatus> allowedFrom(ServiceOrderStatus from) {
        if (from == null) {
            return Set.of();
        }
        return MACHINE.allowedFrom(from);
    }
}
