package com.quetoquenana.pedalpal.media.application.command;

import java.util.Map;
import java.util.UUID;

public record ConfirmUploadCommand(
        String storageKey,
        String providerAssetId,
        UUID authenticatedUserId,
        Long sizeBytes,
        Map<String, Object> metadata
) {}