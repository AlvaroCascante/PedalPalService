package com.quetoquenana.pedalpal.media.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UploadMediaRequest(
        boolean isPublic,

        String referenceType,

        @NotNull(message = "{upload.media.files.null}")
        @Valid
        List<MediaRequest> mediaFiles
) {
}
