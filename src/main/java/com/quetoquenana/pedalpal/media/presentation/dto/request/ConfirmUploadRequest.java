package com.quetoquenana.pedalpal.media.presentation.dto.request;

import java.util.Map;

public record ConfirmUploadRequest(
        String storageKey,
        String providerAssetId,
        Long sizeBytes,
        Map<String, Object> metadata
) {}