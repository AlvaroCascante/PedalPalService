package com.quetoquenana.pedalpal.media.application.model;

import java.time.Instant;
import java.util.Map;

/**
 * Application DTO for signed URL responses from storage providers.
 */
public record SignedUrl(
        String url,
        Instant expiresAt,
        Map<String, String> requiredHeaders
) {}

