package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateLandingItemRequest;
import com.quetoquenana.pedalpal.model.local.LandingPageItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LandingItemService {
    LandingPageItem findById(UUID id);

    LandingPageItem create(CreateLandingItemRequest request);

    LandingPageItem update(UUID id, UpdateLandingItemRequest request);

    void delete(UUID id);

    Page<LandingPageItem> findAll(Pageable pageable);

    Page<LandingPageItem> findByStatus(UUID statusId, Pageable pageable);
}
