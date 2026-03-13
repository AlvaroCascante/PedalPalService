package com.quetoquenana.pedalpal.strava.domain.model;

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
public class StravaAthleteBike {
    private String id;
    private String name;
    private String nickname;
    private BigDecimal distance;
    private boolean primary;
    private boolean retired;
}
