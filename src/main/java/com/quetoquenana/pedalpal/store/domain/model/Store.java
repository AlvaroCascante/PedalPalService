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

    private final Set<StoreLocation> locations = new HashSet<>();

    public void addLocation(StoreLocation location) {
        locations.add(location);
    }
}
