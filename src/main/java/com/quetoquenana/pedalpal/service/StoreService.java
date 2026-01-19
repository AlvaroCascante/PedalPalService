package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateStoreRequest;
import com.quetoquenana.pedalpal.dto.api.request.CreateStoreLocationRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateStoreLocationRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateStoreRequest;
import com.quetoquenana.pedalpal.model.local.Store;
import com.quetoquenana.pedalpal.model.local.StoreLocation;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    Store findById(UUID id);

    Store createStore(CreateStoreRequest request);

    Store updateStore(UUID id, UpdateStoreRequest request);

    void deleteStore(UUID id);

    List<Store> findAll();

    StoreLocation addStoreLocation(UUID storeId, CreateStoreLocationRequest request);

    void removeStoreLocation(UUID locationId);

    StoreLocation updateStoreLocation(UUID locationId, UpdateStoreLocationRequest request);
}
