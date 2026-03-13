package com.quetoquenana.pedalpal.strava.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StravaAthleteBikeResponse(
        String id,

        boolean primary,

        String name,

        String nickname,

        @JsonProperty("resource_state")
        int resourceState,

        boolean retired,

        double distance,

        @JsonProperty("converted_distance")
        Double convertedDistance
) {}
