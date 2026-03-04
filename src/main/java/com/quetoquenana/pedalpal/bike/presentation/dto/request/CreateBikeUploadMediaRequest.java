package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request for generating upload URLs for bike media files.
 */
public record CreateBikeUploadMediaRequest(
        boolean isPublic,

        @NotNull(message = "{bike.upload.media.files.null}")
        @Valid
        List<BikeMediaRequest> mediaFiles
) {
}

