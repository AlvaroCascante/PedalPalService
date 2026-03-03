package com.quetoquenana.pedalpal.media.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record UploadMediaSpecRequest(

        @NotNull(message = "{upload.media.mediaType0.null}")
        String mediaType, // The type of media (e.g., "image", "video", "document", etc.)

        @NotNull(message = "{upload.media.contentType.null}")
        String contentType, // The MIME type of the media (e.g., "image/jpeg", "video/mp4", etc.)

        boolean isPrimary, // Whether this media should be marked as primary for the associated entity

        @NotNull(message = "{upload.media.name.null}")
        String name,

        String altText
) {
}
