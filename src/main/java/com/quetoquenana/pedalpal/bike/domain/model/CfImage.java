package com.quetoquenana.pedalpal.bike.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CfImage extends Auditable {

    private UUID id;

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
