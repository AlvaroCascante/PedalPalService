package com.quetoquenana.pedalpal.store.application.mapper;

import com.quetoquenana.pedalpal.store.application.result.StoreLocationResult;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maps store domain models to application results.
 */
public class StoreMapper {

    /**
     * Builds a result DTO from a domain model.
     */
    public StoreResult toResult(Store model) {
        Set<StoreLocation> locations = model.getLocations() == null ? Collections.emptySet() : model.getLocations();
        return new StoreResult(
                model.getId(),
                model.getName(),
                locations.stream()
                        .map(this::toResult)
                        .collect(Collectors.toSet())
        );
    }

    public StoreLocationResult toResult(StoreLocation model) {
        return new StoreLocationResult(
                model.getId(),
                model.getName(),
                model.getStorePrefix(),
                model.getWebsite(),
                model.getAddress(),
                model.getLatitude(),
                model.getLongitude(),
                model.getPhone(),
                model.getTimezone(),
                model.getStatus()
        );
    }
}
