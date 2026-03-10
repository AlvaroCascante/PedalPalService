package com.quetoquenana.pedalpal.bike.application.result;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;

import java.util.List;
import java.util.UUID;

public record BikeMediaResult(
        UUID id,
        List<MediaResult> mediaResults
) {
}
