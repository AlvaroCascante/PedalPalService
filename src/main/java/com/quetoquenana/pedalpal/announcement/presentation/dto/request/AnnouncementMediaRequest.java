package com.quetoquenana.pedalpal.announcement.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnnouncementMediaRequest(
        @NotBlank(message = "{announcement.media.contentType.blank}")
        @Size(max = 50, message = "{announcement.media.contentType.max}")
        String contentType,

        boolean isPrimary,

        @Size(max = 50, message = "{announcement.media.title.max}")
        String name,

        String altText
) {}
