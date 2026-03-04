package com.quetoquenana.pedalpal.media.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record ConfirmUploadRequest(
        @NotBlank(message = "{media.storageKey.required}")
        String storageKey,

        @NotBlank(message = "{media.providerAssetId.required}")
        String providerAssetId,

        Long sizeBytes,

        Map<String, Object> metadata
) {}