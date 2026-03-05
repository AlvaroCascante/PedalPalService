package com.quetoquenana.pedalpal.store.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.exception.DomainException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Domain model for stores with basic invariants.
 */
@Getter
@Setter
public class Store extends Auditable {

    private UUID id;

    private String name;

    private Set<StoreLocation> locations;

    /**
     * Creates a Store with validated core fields.
     */
    @Builder
    public Store(UUID id,
                 String name,
                 Set<StoreLocation> locations) {
        validateNotBlank("name", name);
        this.id = id;
        this.name = name;
        this.locations = locations;
    }

    private static void validateNotBlank(String field, String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("Store " + field + " is required");
        }
    }

    public void addLocation(StoreLocation location) {
        if (this.locations == null) {
            this.locations = new HashSet<>();
        }
        locations.add(location);
    }
}
