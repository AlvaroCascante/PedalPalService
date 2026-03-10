package com.quetoquenana.pedalpal.media.application.result;

import java.util.Map;
import java.util.UUID;

/**
 * Result payload for stored media metadata.
 */
public record ConfirmedUploadResult(
        UUID mediaId,
        String storageKey,
        String status,
        String cdnUrl,
        String mediaType,
        String contentType,
        Boolean isPrimary,
        String provider,
        String providerAssetId,
        Long sizeBytes,
        String title,
        String altText,
        Map<String, Object> metadata,
        UUID referenceId,
        String referenceType,
        UUID ownerId
) {}
