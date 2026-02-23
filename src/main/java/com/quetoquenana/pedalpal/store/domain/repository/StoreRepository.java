package com.quetoquenana.pedalpal.store.domain.repository;

import com.quetoquenana.pedalpal.store.domain.model.Store;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository {

    Optional<Store> getById(UUID id);

    List<Store> getAll();

    Store save(Store store);
}