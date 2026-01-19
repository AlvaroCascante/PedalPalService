package com.quetoquenana.pedalpal.dto.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCfImageRequest {

    @NotBlank(message = "{image.create.provider.blank}")
    private String provider;

    @NotBlank(message = "{image.create.providerAssetId.blank}")
    private String providerAssetId;

    @NotNull(message = "{image.create.ownerId.required}")
    private UUID ownerId;

    // references system_codes.id (stored as UUID)
    private UUID contextCode;

    private UUID referenceId;

    private Integer position;

    private Boolean isPrimary;

    private String title;

    private String altText;

    // metadata as JSON map
    private Map<String, Object> metadata;
}
