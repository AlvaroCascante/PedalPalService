package com.quetoquenana.pedalpal.media.application.command;

import java.util.UUID;

public record UploadMediaSpecCommand(
        UUID id,
        String contentType, // The MIME type of the media (e.g., "image/jpeg", "video/mp4", etc.)
        String name,
        String altText
) {
}
