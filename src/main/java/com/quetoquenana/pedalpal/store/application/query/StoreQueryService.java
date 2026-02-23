package com.quetoquenana.pedalpal.store.application.query;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.store.application.mapper.StoreMapper;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class StoreQueryService {

    private final StoreMapper mapper;
    private final StoreRepository repository;

    public StoreResult getById(UUID id) {
        Store bike = repository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toBikeResult(bike);
    }

    public List<StoreResult> getAll() {
        List<Store> bikes = repository.getAll();

        return bikes.stream()
                .map(mapper::toBikeResult)
                .toList();
    }
}
