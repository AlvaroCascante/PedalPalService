package com.quetoquenana.pedalpal.bike.domain.model;

public record BikeChangeItem(
        BikeField field,
        Object oldValue,
        Object newValue
) {
    public static BikeChangeItem of(BikeField field, Object oldValue, Object newValue) {
        return new BikeChangeItem(field, oldValue, newValue);
    }
}