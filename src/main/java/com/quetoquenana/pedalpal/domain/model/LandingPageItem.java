package com.quetoquenana.pedalpal.domain.model;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
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
