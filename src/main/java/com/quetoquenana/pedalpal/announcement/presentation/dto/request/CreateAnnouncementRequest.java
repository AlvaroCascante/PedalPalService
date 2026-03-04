package com.quetoquenana.pedalpal.announcement.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateAnnouncementRequest(
        @NotBlank(message = "{announcement.title.blank}")
        @Size(max = 50, message = "{announcement.title.max}")
        String title,

        @Size(max = 100, message = "{announcement.subtitle.max}")
        String subTitle,

        @Size(max = 250, message = "{announcement.description.max}")
        String description,

        @Min(value = 1, message = "{announcement.position.invalid}")
        Integer position,

        @Size(max = 250, message = "{announcement.url.max}")
        String url,

        @Valid
        List<AnnouncementMediaRequest> mediaFiles
) {
}
