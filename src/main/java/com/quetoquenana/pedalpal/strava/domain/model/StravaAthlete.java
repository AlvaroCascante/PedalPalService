package com.quetoquenana.pedalpal.strava.domain.model;

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
public class StravaAthlete {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String city;
    private String state;
    private String country;
    private String sex;
    private String measurementPreference;
    private List<StravaAthleteBike> bikes;
}
