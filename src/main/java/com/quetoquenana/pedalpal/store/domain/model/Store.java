package com.quetoquenana.pedalpal.store.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Store extends Auditable {

    private UUID id;

    private String name;

    private Set<StoreLocation> locations;

    public void addLocation(StoreLocation location) {
        if (this.locations == null) {
            this.locations = new HashSet<>();
        }
        locations.add(location);
    }
}
