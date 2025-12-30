package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.StoreLocation;

import java.util.List;
import java.util.UUID;

public interface StoreRepositoryCustom {
    StoreLocation createLocation(UUID storeId, StoreLocation location);
    StoreLocation updateLocation(StoreLocation location);
    void deleteLocation(UUID locationId);
    List<StoreLocation> findLocationsByStoreId(UUID storeId);
}

