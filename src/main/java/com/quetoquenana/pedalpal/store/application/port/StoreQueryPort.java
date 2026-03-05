package com.quetoquenana.pedalpal.store.application.port;

import com.quetoquenana.pedalpal.store.application.result.StoreLocationResult;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;

import java.util.List;
import java.util.UUID;

/**
 * Application port for read-only store queries.
 */
public interface StoreQueryPort {

    /**
     * Retrieves a store by id.
     *
     * @param id the id of the store
     * @return the store with the given id
     */
    StoreResult getById(UUID id);

    /**
     * Retrieves all stores.
     *
     * @return a list of all stores
     */
    List<StoreResult> getAll();

    /**
     * Retrieves a storeLocation by id.
     *
     * @param id the id of the store
     * @return the store with the given id
     */
    StoreLocationResult getLocationById(UUID id);
}
