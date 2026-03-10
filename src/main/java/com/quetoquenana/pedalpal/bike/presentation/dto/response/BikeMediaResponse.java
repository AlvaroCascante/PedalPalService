package com.quetoquenana.pedalpal.bike.presentation.dto.response;

import com.quetoquenana.pedalpal.media.presentation.dto.response.MediaResponse;

import java.util.List;
import java.util.UUID;

/**
 * Response with generated upload metadata for bike media files.
 */
public record BikeMediaResponse(
        UUID id,
        List<MediaResponse> mediaUrlResponse
) {
}

