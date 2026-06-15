package com.quetoquenana.pedalpal.strava.presentation.dto.response;

import java.math.BigDecimal;

public record StravaAthleteBikeResponse(
        String id,
        String name,
        String nickname,
        boolean primary,
        boolean retired,
        BigDecimal distance,
        String brandName,
        String modelName,
        Integer frameType,
        String description
) {}