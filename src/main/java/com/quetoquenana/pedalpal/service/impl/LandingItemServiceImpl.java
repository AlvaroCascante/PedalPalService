package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.data.LandingPageItem;
import com.quetoquenana.pedalpal.model.data.SystemCode;
import com.quetoquenana.pedalpal.repository.LandingPageItemRepository;
import com.quetoquenana.pedalpal.service.LandingItemService;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LandingItemServiceImpl implements LandingItemService {

    private final LandingPageItemRepository repository;
    private final SystemCodeService systemCodeService;

    @Override
    public LandingPageItem findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("landing.item.not.found", id));
    }

    @Override
    @Transactional
    public LandingPageItem create(CreateLandingItemRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        SystemCode status = systemCodeService.findByCategoryAndCode("LANDING_PAGE_STATUS", "ACTIVE")
                .orElseThrow(() -> new RecordNotFoundException("landing.item.status.not.found", "ACTIVE"));

        LandingPageItem item = LandingPageItem.createFromRequest(request);
        item.setStatus(status);
        LocalDateTime now = LocalDateTime.now();
        item.setCreatedAt(now);
        item.setCreatedBy(user.username());
        item.setUpdatedAt(now);
        item.setUpdatedBy(user.username());
        return repository.save(item);
    }

    @Override
    @Transactional
    public LandingPageItem update(UUID id, UpdateLandingItemRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        LandingPageItem item = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("landing.item.not.found", id));
        item.updateFromRequest(request);
        if (request.getStatusId() != null) {
            SystemCode status = systemCodeService.findById(request.getStatusId())
                    .orElseThrow(() -> new RecordNotFoundException("landing.item.status.not.found", request.getStatusId()));
            item.setStatus(status);
        }
        item.setUpdatedAt(LocalDateTime.now());
        item.setUpdatedBy(user.username());
        return repository.save(item);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        LandingPageItem item = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("landing.item.not.found", id));
        repository.delete(item);
    }

    @Override
    public Page<LandingPageItem> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<LandingPageItem> findByStatus(UUID statusId, Pageable pageable) {
        return repository.findByStatusId(statusId, pageable);
    }
}
