package com.quetoquenana.pedalpal.announcement.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateAnnouncementRequest(
        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{announcement.update.title.blank}")
        @Size(max = 50, message = "{announcement.update.title.max}")
        String title,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{announcement.update.subtitle.blank}")
        @Size(max = 250, message = "{announcement.update.subtitle.max}")
        String subTitle,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(max = 250, message = "{announcement.update.description.max}")
        String description,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 1, message = "{announcement.update.position.invalid}")
        Integer position,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{announcement.update.url.blank}")
        @Size(max = 500, message = "{announcement.update.url.max}")
        String url
) {
}
