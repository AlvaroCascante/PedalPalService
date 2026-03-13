package com.quetoquenana.pedalpal.strava.application.result;

import java.math.BigDecimal;

public record StravaAthleteBikeResult(
        String id,
        String name,
        String nickname,
        boolean primary,
        boolean retired,
        BigDecimal distance
) {}