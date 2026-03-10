package com.quetoquenana.pedalpal.media.application.command;

public record UploadMediaSpecCommand(
        String mediaType, // The type of media (e.g., "image", "video", "document", etc.)
        String contentType, // The MIME type of the media (e.g., "image/jpeg", "video/mp4", etc.)
        boolean isPrimary, // Whether this media should be marked as primary for the associated entity
        String name,
        String altText
) {
}
