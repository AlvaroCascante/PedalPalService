package com.quetoquenana.pedalpal.store.application.query;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.store.application.mapper.StoreMapper;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Read-only query service for store data.
 */
@RequiredArgsConstructor
public class StoreQueryService {

    private final StoreMapper mapper;
    private final StoreRepository repository;

    /**
     * Retrieves a store by id.
     */
    public StoreResult getById(UUID id) {
        Store model = repository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toResult(model);
    }

    /**
     * Retrieves all stores.
     */
    public List<StoreResult> getAll() {
        List<Store> models = repository.getAll();

        return models.stream()
                .map(mapper::toResult)
                .toList();
    }
}
