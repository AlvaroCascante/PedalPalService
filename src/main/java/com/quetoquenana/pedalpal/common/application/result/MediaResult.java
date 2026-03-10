package com.quetoquenana.pedalpal.common.application.result;

import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;

import java.time.Instant;
import java.util.UUID;

public record MediaResult(
        UUID id,
        String contentType,
        String provider,
        Boolean isPrimary,
        MediaStatus status,
        String name,
        String altText,
        String url,
        Instant expiresAt
) {}
