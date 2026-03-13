package com.quetoquenana.pedalpal.strava.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Strava activity payload used for bike usage sync.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StravaAthleteResponse {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String city;
    private String state;
    private String country;
    private String sex;

    @JsonProperty("measurement_preference")
    private String measurementPreference;

    private List<StravaAthleteBikeResponse> bikes;
}
