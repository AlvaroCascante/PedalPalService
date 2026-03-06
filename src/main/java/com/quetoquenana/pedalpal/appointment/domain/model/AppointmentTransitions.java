package com.quetoquenana.pedalpal.appointment.domain.model;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines allowed appointment status transitions.
 */
public final class AppointmentTransitions {

    private static final Map<AppointmentStatus, Set<AppointmentStatus>> RULES;

    static {
        Map<AppointmentStatus, Set<AppointmentStatus>> rules = new EnumMap<>(AppointmentStatus.class);
        rules.put(AppointmentStatus.REQUESTED, EnumSet.of(AppointmentStatus.CONFIRMED, AppointmentStatus.REJECTED, AppointmentStatus.CANCELED));
        rules.put(AppointmentStatus.CONFIRMED, EnumSet.of(AppointmentStatus.BIKE_RECEIVED, AppointmentStatus.NO_SHOW, AppointmentStatus.CANCELED));
        rules.put(AppointmentStatus.BIKE_RECEIVED, EnumSet.of(AppointmentStatus.IN_PROGRESS, AppointmentStatus.CANCELED));
        rules.put(AppointmentStatus.IN_PROGRESS, EnumSet.of(AppointmentStatus.COMPLETED, AppointmentStatus.CANCELED));
        rules.put(AppointmentStatus.COMPLETED, EnumSet.of(AppointmentStatus.BIKE_RETURNED));
        rules.put(AppointmentStatus.BIKE_RETURNED, EnumSet.noneOf(AppointmentStatus.class));
        rules.put(AppointmentStatus.CANCELED, EnumSet.of(AppointmentStatus.BIKE_RETURNED));
        rules.put(AppointmentStatus.REJECTED, EnumSet.noneOf(AppointmentStatus.class));
        rules.put(AppointmentStatus.NO_SHOW, EnumSet.noneOf(AppointmentStatus.class));
        rules.put(AppointmentStatus.UNKNOWN, EnumSet.noneOf(AppointmentStatus.class));
        RULES = Map.copyOf(rules);
    }

    private AppointmentTransitions() {
    }

    /**
     * Checks if transition is allowed.
     */
    public static boolean isAllowed(AppointmentStatus from, AppointmentStatus to) {
        if (from == null || to == null) {
            return false;
        }
        return RULES.getOrDefault(from, Set.of()).contains(to);
    }
}

