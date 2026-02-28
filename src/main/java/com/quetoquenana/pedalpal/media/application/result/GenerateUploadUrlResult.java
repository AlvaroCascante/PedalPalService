package com.quetoquenana.pedalpal.media.application.result;

import java.time.Instant;
import java.util.UUID;

public record GenerateUploadUrlResult(
        UUID mediaId,
        String uploadUrl,
        String storageKey,
        Instant expiresAt
) {}
