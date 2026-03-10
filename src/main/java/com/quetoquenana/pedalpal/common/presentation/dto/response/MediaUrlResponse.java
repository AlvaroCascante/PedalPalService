package com.quetoquenana.pedalpal.common.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

public record MediaUrlResponse(
        UUID mediaId,
        String uploadUrl,
        Instant expiresAt
) {}