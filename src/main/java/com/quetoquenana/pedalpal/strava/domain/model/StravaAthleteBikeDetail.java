package com.quetoquenana.pedalpal.strava.domain.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;

/**
 * Strava activity payload used for bike usage sync.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StravaAthleteBikeDetail {
    private String id;
    private boolean primary;
    private String name;
    private String nickname;
    private Integer resourceState;
    private boolean retired;
    private BigDecimal distance;
    private BigDecimal convertedDistance;
    private String brandName;
    private String modelName;
    private Integer frameType;
    private String description;
    private Long weight;
}
