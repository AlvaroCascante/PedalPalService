package com.quetoquenana.pedalpal.application.command;

import java.util.UUID;

/**
 * Application command for PATCH bike updates.
 * ownerId is intentionally not present (cannot be updated).
 * Nullable fields represent "not provided".
 */
public record UpdateBikeStatusCommand(
        UUID bikeId,
        UUID authenticatedUserId,
        String status
) { }

