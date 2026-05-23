package com.quetoquenana.pedalpal.media.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MediaRequest(

        @NotNull(message = "{upload.media.id.null}")
        UUID id,

        @NotNull(message = "{upload.media.contentType.null}")
        String contentType,

        @NotNull(message = "{upload.media.name.null}")
        String name,

        String altText
) {
}
