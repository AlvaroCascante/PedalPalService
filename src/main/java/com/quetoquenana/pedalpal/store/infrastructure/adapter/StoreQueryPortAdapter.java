package com.quetoquenana.pedalpal.store.infrastructure.adapter;

import com.quetoquenana.pedalpal.store.application.port.StoreQueryPort;
import com.quetoquenana.pedalpal.store.application.query.StoreQueryService;
import com.quetoquenana.pedalpal.store.application.result.StoreLocationResult;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Infrastructure adapter exposing store queries via the application port.
 */
@Component
@RequiredArgsConstructor
class StoreQueryPortAdapter implements StoreQueryPort {

    private final StoreQueryService queryService;

    /**
     * Retrieves a store by id.
     */
    @Override
    public StoreResult getById(UUID id) {
        return queryService.getById(id);
    }

    /**
     * Retrieves all stores.
     */
    @Override
    public List<StoreResult> getAll() {
        return queryService.getAll();
    }

    /**
     * Retrieves a storeLocation by id.
     */
    @Override
    public StoreLocationResult getLocationById(UUID id) {
        return queryService.getLocationById(id);
    }
}
