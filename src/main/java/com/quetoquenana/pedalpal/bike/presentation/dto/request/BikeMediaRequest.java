package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request item for a single bike media upload specification.
 */
public record BikeMediaRequest(
        @NotBlank(message = "{bike.upload.media.contentType.blank}")
        @Size(max = 50, message = "{bike.upload.media.contentType.max}")
        String contentType,

        @NotBlank(message = "{bike.upload.media.mediaType.blank}")
        @Size(max = 50, message = "{bike.upload.media.mediaType.max}")
        String mediaType,

        boolean isPrimary,

        @Size(max = 50, message = "{bike.upload.media.name.max}")
        String name,

        @Size(max = 250, message = "{bike.upload.media.altText.max}")
        String altText
) {
}

