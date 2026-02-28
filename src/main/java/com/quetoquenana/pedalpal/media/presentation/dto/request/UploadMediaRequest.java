package com.quetoquenana.pedalpal.media.presentation.dto.request;

import java.util.UUID;

public record UploadMediaRequest(
        UUID ownerId,
        UUID referenceId,
        String referenceType,
        String mediaType,
        String contentType,
        boolean isPrimary,
        String title,
        String altText
) {}
