package com.quetoquenana.pedalpal.media.application.command;

import java.util.UUID;

public record GenerateUploadUrlCommand(
        UUID ownerId,
        UUID authenticatedUserId,
        boolean isAdmin,
        UUID referenceId,
        String referenceType,
        boolean isPrimary,
        String mediaType,
        String contentType,
        String title,
        String altText,
        String provider
) {}