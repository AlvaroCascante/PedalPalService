package com.quetoquenana.pedalpal.bike.application.command;

/**
 * Command item for a single bike media upload specification.
 */
public record BikeMediaCommand(
        String contentType,
        String mediaType,
        boolean isPrimary,
        String title,
        String altText
) {
}

