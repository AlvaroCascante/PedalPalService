package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.CreateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.UpdateLandingItemRequest;
import com.quetoquenana.pedalpal.model.LandingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LandingItemService {
    LandingItem create(CreateLandingItemRequest req);
    LandingItem update(UUID id, UpdateLandingItemRequest req);
    LandingItem findById(UUID id);
    List<LandingItem> findAll();
    Page<LandingItem> findAll(Pageable pageable);
    Page<LandingItem> findByStatus(String status, Pageable pageable);
    Page<LandingItem> findByCategory(String category, Pageable pageable);
    void delete(UUID id);
}
