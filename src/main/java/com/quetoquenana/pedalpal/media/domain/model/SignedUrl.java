package com.quetoquenana.pedalpal.media.domain.model;

import java.time.Instant;
import java.util.Map;

public record SignedUrl(
        String url,
        Instant expiresAt,
        Map<String, String> requiredHeaders // often just Content-Type (optional)
) {}