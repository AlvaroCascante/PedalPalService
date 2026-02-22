package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class LandingPageItem extends Auditable {

    private UUID id;

    private String title;

    private String subtitle;

    private String description;

    private Integer position;

    private String url;

    private SystemCode status;
}
