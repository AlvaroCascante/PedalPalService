package com.quetoquenana.pedalpal.strava.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Strava activity payload used for bike usage sync.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StravaActivity {
    private Long id;
    private Long athleteId;
    private String gearId;
    private StravaSportType sportType;
    private Long distanceMeters;
    private Long movingTimeSeconds;
}
