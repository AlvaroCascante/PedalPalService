package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.CreateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.UpdateLandingItemRequest;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.LandingCategory;
import com.quetoquenana.pedalpal.model.LandingItem;
import com.quetoquenana.pedalpal.model.LandingStatus;
import com.quetoquenana.pedalpal.repository.LandingItemRepository;
import com.quetoquenana.pedalpal.service.LandingItemService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LandingItemServiceImpl implements LandingItemService {

    private final LandingItemRepository repository;

    public LandingItemServiceImpl(LandingItemRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public LandingItem create(CreateLandingItemRequest req) {
        LandingItem item = LandingItem.createFromRequest(req);
        return repository.save(item);
    }

    @Override
    @Transactional
    public LandingItem update(UUID id, UpdateLandingItemRequest req) {
        LandingItem item = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("landing.not.found", id));
        item.updateFromRequest(req);
        return repository.save(item);
    }

    @Override
    public LandingItem findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("landing.not.found", id));
    }

    @Override
    public List<LandingItem> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<LandingItem> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<LandingItem> findByStatus(String status, Pageable pageable) {
        LandingStatus s = LandingStatus.fromString(status);
        return repository.findByStatus(s, pageable);
    }

    @Override
    public Page<LandingItem> findByCategory(String category, Pageable pageable) {
        LandingCategory c = LandingCategory.fromString(category);
        return repository.findByCategory(c, pageable);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        LandingItem item = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("landing.not.found", id));
        // soft delete: mark as INACTIVE
        item.setStatus(LandingStatus.INACTIVE);
        repository.save(item);
    }
}
