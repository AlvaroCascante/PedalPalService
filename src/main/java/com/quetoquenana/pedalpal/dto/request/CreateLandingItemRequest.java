package com.quetoquenana.pedalpal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

    private Integer position;

    @Size(max = 500, message = "{landing.create.url.max}")
    private String url;

    @NotNull(message = "{landing.create.status.required}")
    private UUID statusId;
}
