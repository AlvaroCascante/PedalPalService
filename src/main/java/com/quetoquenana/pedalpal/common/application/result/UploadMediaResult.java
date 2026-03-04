package com.quetoquenana.pedalpal.common.application.result;

import java.time.Instant;
import java.util.UUID;

public record UploadMediaResult(
        UUID mediaId,
        String uploadUrl,
        String storageKey,
        Instant expiresAt
) {}
