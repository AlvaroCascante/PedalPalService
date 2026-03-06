package com.quetoquenana.pedalpal.bike.application.command;

import java.util.List;
import java.util.UUID;

/**
 * Command for generating bike media upload URLs.
 */
public record CreateBikeUploadMediaCommand(
        UUID bikeId,
        boolean isPublic,
        List<BikeMediaCommand> mediaFiles
) {
}

