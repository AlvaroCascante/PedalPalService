package com.quetoquenana.pedalpal.strava.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Strava activity response payload.
 */
@Getter
@Setter
@NoArgsConstructor
public class StravaActivityResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("gear_id")
    private String gearId;

    @JsonProperty("sport_type")
    private String sportType;

    @JsonProperty("distance")
    private Long distance;

    @JsonProperty("moving_time")
    private Long movingTime;
}
