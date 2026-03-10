package com.quetoquenana.pedalpal.media.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

public record MediaResponse(
        UUID id,
        String contentType,
        String provider,
        Boolean isPrimary,
        String status,
        String name,
        String altText,
        String url,
        Instant expiresAt
) {}