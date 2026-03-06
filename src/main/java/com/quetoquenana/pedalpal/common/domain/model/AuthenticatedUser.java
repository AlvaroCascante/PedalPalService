package com.quetoquenana.pedalpal.common.domain.model;

import java.util.UUID;

/**
 * Minimal representation of an authenticated user for application/business code.
 * <p>
 * Avoids leaking framework-specific security types into controllers and use cases.
 */
public record AuthenticatedUser(
        UUID userId,
        String username,
        String name,
        UserType type
) {
}

