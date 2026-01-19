package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateCfImageRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateCfImageRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.local.CfImage;
import com.quetoquenana.pedalpal.repository.CfImageRepository;
import com.quetoquenana.pedalpal.service.CfImageService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CfImageServiceImpl implements CfImageService {

    private final CfImageRepository repository;

    @Override
    public Page<CfImage> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public CfImage findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("image.not.found", id));
    }

    @Override
    @Transactional
    public CfImage createImage(CreateCfImageRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        CfImage img = CfImage.createFromRequest(request);
        LocalDateTime now = LocalDateTime.now();
        img.setCreatedAt(now);
        img.setUpdatedAt(now);
        img.setCreatedBy(user.username());
        img.setUpdatedBy(user.username());
        return repository.save(img);
    }

    @Override
    @Transactional
    public CfImage updateImage(UUID id, UpdateCfImageRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        CfImage existing = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("image.not.found", id));
        existing.updateFromRequest(request);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(user.username());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void deleteImage(UUID id) {
        CfImage existing = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("image.not.found", id));
        repository.delete(existing);
    }

    @Override
    public List<CfImage> findByProvider(String provider) {
        return repository.findByProvider(provider);
    }

    @Override
    public List<CfImage> findByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId);
    }

    @Override
    public Optional<CfImage> findByProviderAssetId(String providerAssetId) {
        return repository.findByProviderAssetId(providerAssetId);
    }

    @Override
    public List<CfImage> findByContextCode(UUID contextCode) {
        return repository.findByContextCode(contextCode);
    }

    @Override
    public List<CfImage> findByReferenceId(UUID referenceId) {
        return repository.findByReferenceId(referenceId);
    }
}

