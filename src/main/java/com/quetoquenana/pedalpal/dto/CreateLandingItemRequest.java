package com.quetoquenana.pedalpal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLandingItemRequest {
    @NotBlank(message = "{landing.create.title.blank}")
    @Size(max = 200, message = "{landing.create.title.max}")
    private String title;

    @Size(max = 200, message = "{landing.create.subtitle.max}")
    private String subtitle;

    @Size(max = 2000, message = "{landing.create.description.max}")
    private String description;

    @NotBlank(message = "{landing.create.category.required}")
    private String category;

    @NotBlank(message = "{landing.create.status.required}")
    private String status;

    private Instant startAt;

    private Instant endAt;

    @Size(max = 500, message = "{landing.create.imageUrl.max}")
    private String imageUrl;

    @Size(max = 500, message = "{landing.create.linkUrl.max}")
    private String linkUrl;

    private Integer priority;

    private String metadata; // JSON string
}

