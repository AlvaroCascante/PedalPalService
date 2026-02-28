package com.quetoquenana.pedalpal.media.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

public record GenerateUploadUrlResponse(
        UUID mediaId,
        String uploadUrl,
        String storageKey,
        Instant expiresAt
) {}