package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateCfImageRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateCfImageRequest;
import com.quetoquenana.pedalpal.model.data.CfImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CfImageService {

    Page<CfImage> findAll(Pageable pageable);

    CfImage findById(UUID id);

    CfImage createImage(CreateCfImageRequest request);

    CfImage updateImage(UUID id, UpdateCfImageRequest request);

    void deleteImage(UUID id);

    List<CfImage> findByProvider(String provider);

    List<CfImage> findByOwnerId(UUID ownerId);

    Optional<CfImage> findByProviderAssetId(String providerAssetId);

    List<CfImage> findByContextCode(UUID contextCode);

    List<CfImage> findByReferenceId(UUID referenceId);
}

