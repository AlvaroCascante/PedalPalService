package com.quetoquenana.pedalpal.announcement.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CreateAnnouncementRequest(
        @Size(min = 1, message = "{announcement.create.title.blank}")
        @Size(max = 50, message = "{announcement.create.title.max}")
        String title,

        @Size(max = 250, message = "{announcement.create.subtitle.max}")
        String subTitle,

        @Size(max = 250, message = "{announcement.create.description.max}")
        String description,

        @Min(value = 1, message = "{announcement.create.position.invalid}")
        Integer position,

        @Size(max = 250, message = "{announcement.create.url.max}")
        String url
) {
}
