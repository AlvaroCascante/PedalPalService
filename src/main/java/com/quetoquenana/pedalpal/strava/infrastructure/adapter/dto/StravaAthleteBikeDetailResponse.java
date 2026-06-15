package com.quetoquenana.pedalpal.strava.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StravaAthleteBikeDetailResponse(
        String id,

        boolean primary,

        String name,

        String nickname,

        @JsonProperty("resource_state")
        int resourceState,

        boolean retired,

        double distance,

        @JsonProperty("converted_distance")
        Double convertedDistance,

        @JsonProperty("brand_name")
        String brandName,

        @JsonProperty("model_name")
        String modelName,

        @JsonProperty("frame_type")
        Integer frameType,

        String description,

        Long weight
) {}
