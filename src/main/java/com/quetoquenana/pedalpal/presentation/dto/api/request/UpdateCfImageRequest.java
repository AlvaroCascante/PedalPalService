package com.quetoquenana.pedalpal.presentation.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCfImageRequest {

    private String provider;

    private String providerAssetId;

    private UUID ownerId;

    private UUID contextCode;

    private UUID referenceId;

    private Integer position;

    private Boolean isPrimary;

    private String title;

    private String altText;

    private Map<String, Object> metadata;
}

