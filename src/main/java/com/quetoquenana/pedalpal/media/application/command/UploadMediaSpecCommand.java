package com.quetoquenana.pedalpal.media.application.command;

public record UploadMediaSpecCommand(
        String contentType, // The MIME type of the media (e.g., "image/jpeg", "video/mp4", etc.)
        String name,
        String altText
) {
}
