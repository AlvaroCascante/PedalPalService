package com.quetoquenana.pedalpal.announcement.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnnouncementMediaRequest(
        @NotBlank(message = "{announcement.create.media.contentType.blank}")
        @Size(max = 50, message = "{announcement.create.media.contentType.max}")
        String contentType,

        @NotBlank(message = "{announcement.create.media.mediaType.blank}")
        @Size(max = 50, message = "{announcement.create.media.mediaType.max}")
        String mediaType,

        boolean isPrimary,

        @Size(max = 50, message = "{announcement.create.media.title.max}")
        String title,
        String altText
) {}
