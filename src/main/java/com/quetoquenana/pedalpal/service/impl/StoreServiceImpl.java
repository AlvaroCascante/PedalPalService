package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateStoreRequest;
import com.quetoquenana.pedalpal.dto.api.request.CreateStoreLocationRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateStoreLocationRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateStoreRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.data.Store;
import com.quetoquenana.pedalpal.model.data.StoreLocation;
import com.quetoquenana.pedalpal.repository.StoreLocationRepository;
import com.quetoquenana.pedalpal.repository.StoreRepository;
import com.quetoquenana.pedalpal.service.StoreService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreLocationRepository storeLocationRepository;

    public StoreServiceImpl(StoreRepository storeRepository, StoreLocationRepository storeLocationRepository) {
        this.storeRepository = storeRepository;
        this.storeLocationRepository = storeLocationRepository;
    }

    @Override
    public Store findById(UUID id) {
        return storeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("store.not.found", id));
    }

    @Override
    @Transactional
    public Store createStore(CreateStoreRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Store store = Store.createFromRequest(request);
        LocalDateTime now = LocalDateTime.now();
        store.setCreatedAt(now);
        store.setCreatedBy(user.username());
        store.setUpdatedAt(now);
        store.setUpdatedBy(user.username());
        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public Store updateStore(UUID id, UpdateStoreRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Store store = storeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("store.not.found", id));
        store.updateFromRequest(request);
        store.setUpdatedAt(LocalDateTime.now());
        store.setUpdatedBy(user.username());
        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public void deleteStore(UUID id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("store.not.found", id));
        storeRepository.delete(store);
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    @Transactional
    public StoreLocation addStoreLocation(UUID storeId, CreateStoreLocationRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RecordNotFoundException("store.not.found", storeId));
        StoreLocation loc = StoreLocation.createFromRequest(request, store);
        LocalDateTime now = LocalDateTime.now();
        loc.setCreatedAt(now);
        loc.setCreatedBy(user.username());
        loc.setUpdatedAt(now);
        loc.setUpdatedBy(user.username());
        return storeLocationRepository.save(loc);
    }

    @Override
    @Transactional
    public void removeStoreLocation(UUID locationId) {
        StoreLocation loc = storeLocationRepository.findById(locationId).orElseThrow(() -> new RecordNotFoundException("store.location.not.found", locationId));
        storeLocationRepository.delete(loc);
    }

    @Override
    @Transactional
    public StoreLocation updateStoreLocation(UUID locationId, UpdateStoreLocationRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        StoreLocation loc = storeLocationRepository.findById(locationId).orElseThrow(() -> new RecordNotFoundException("store.location.not.found", locationId));

        loc.updateFromRequest(request);

        loc.setUpdatedAt(LocalDateTime.now());
        loc.setUpdatedBy(user.username());

        return storeLocationRepository.save(loc);
    }
}
