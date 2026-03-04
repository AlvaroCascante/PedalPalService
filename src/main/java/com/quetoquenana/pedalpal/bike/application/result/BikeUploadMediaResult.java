package com.quetoquenana.pedalpal.bike.application.result;

import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;

import java.util.Set;

/**
 * Use case result containing generated upload metadata for bike media files.
 */
public record BikeUploadMediaResult(
        Set<UploadMediaResult> uploadMediaResults
) {
}

