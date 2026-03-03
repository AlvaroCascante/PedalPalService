package com.quetoquenana.pedalpal.media.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record UploadMediaRequest(
        @NotNull(message = "{upload.media.referenceId.null}")
        UUID referenceId,  // The ID of the entity this media is associated with (e.g., a post, user profile, etc.)

        @NotNull(message = "{upload.media.referenceType.null}")
        String referenceType, // The type of the entity (e.g., "post", "profile", etc.)

        @NotNull(message = "{upload.media.mediaSpecs.null}")
        @Valid
        List<UploadMediaSpecRequest> mediaSpecs // A set of media specifications for the files being uploaded
) {}
