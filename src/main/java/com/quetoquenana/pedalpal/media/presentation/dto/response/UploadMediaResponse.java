package com.quetoquenana.pedalpal.media.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UploadMediaResponse(
        UUID mediaId,
        String storageKey,
        String uploadUrl,
        Instant expiresAt
) {}