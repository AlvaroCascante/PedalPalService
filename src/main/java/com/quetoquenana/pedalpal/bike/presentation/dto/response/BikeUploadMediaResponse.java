package com.quetoquenana.pedalpal.bike.presentation.dto.response;

import com.quetoquenana.pedalpal.media.presentation.dto.response.UploadMediaResponse;

import java.util.Set;

/**
 * Response with generated upload metadata for bike media files.
 */
public record BikeUploadMediaResponse(
        Set<UploadMediaResponse> uploadMediaResponse
) {
}

