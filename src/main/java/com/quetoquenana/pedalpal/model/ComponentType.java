package com.quetoquenana.pedalpal.model;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum ComponentType {

    // Drivetrain
    CHAIN("component.chain"),
    CASSETTE("component.cassette"),
    CHAINRINGS("component.chainrings"),
    DERAILLEUR_FRONT("component.derailleur_front"),
    DERAILLEUR_REAR("component.derailleur_rear"),
    SHIFTERS("component.shifters"),

    // Braking
    BRAKE_PADS("component.brake_pads"),
    BRAKE_ROTORS("component.brake_rotors"),
    BRAKE_CALIPERS("component.brake_calipers"),

    // Wheels & tires
    TIRES("component.tires"),
    TUBES("component.tubes"),
    SEALANT("component.sealant"),
    RIMS("component.rims"),
    SPOKES("component.spokes"),

    // Bearings & rotation
    BOTTOM_BRACKET("component.bottom_bracket"),
    HEADSET("component.headset"),
    HUBS("component.hubs"),
    BEARINGS("component.bearings"),

    // Cockpit & contact points
    SADDLE("component.saddle"),
    SEAT_POST("component.seatpost"),
    HANDLEBAR("component.handlebar"),
    STEM("component.stem"),
    GRIPS("component.grips"),
    BAR_TAPE("component.bar_tape"),
    PEDALS("component.pedals"),

    // Suspension (optional but common)
    FORK("component.fork"),
    REAR_SHOCK("component.rear_shock"),

    // Electrical (for e-bikes)
    BATTERY("component.battery"),
    MOTOR("component.motor"),
    CONTROLLER("component.controller"),

    // Fallback
    OTHER("component.other");

    private final String messageKey;

    ComponentType(String messageKey) {
        this.messageKey = messageKey;
    }

    // Resolve the localized label using a Spring MessageSource and Locale.
    // Falls back to a humanized enum name if the key is not found.
    public String resolveLabel(MessageSource messageSource, Locale locale) {
        String defaultLabel = humanizeName();
        return messageSource.getMessage(messageKey, null, defaultLabel, locale);
    }

    // Returns a list of option objects with name, messageKey and localized label
    public static List<Option> localizedOptions(MessageSource messageSource, Locale locale) {
        List<Option> list = new ArrayList<>(values().length);
        for (ComponentType t : values()) {
            String label = t.resolveLabel(messageSource, locale);
            list.add(new Option(t.name(), t.messageKey, label));
        }
        return list;
    }

    // Convenience overload that uses current locale
    public static List<Option> localizedOptions(MessageSource messageSource) {
        return localizedOptions(messageSource, LocaleContextHolder.getLocale());
    }

    public record Option(String name, String messageKey, String label) { }

    // Helper that converts enum name to a readable default: BOTTOM_BRACKET -> "Bottom bracket"
    private String humanizeName() {
        String s = name().toLowerCase().replace('_', ' ');
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Parse a string to a ComponentType. Returns OTHER when input is null or invalid.
     */
    public static ComponentType fromString(String value) {
        if (value == null) return OTHER;
        try {
            String v = value.trim().toUpperCase().replace(' ', '_');
            return ComponentType.valueOf(v);
        } catch (IllegalArgumentException ex) {
            return OTHER;
        }
    }
}