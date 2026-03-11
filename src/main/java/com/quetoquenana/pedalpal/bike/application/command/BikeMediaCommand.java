package com.quetoquenana.pedalpal.bike.application.command;

/**
 * Command item for a single bike media upload specification.
 */
public record BikeMediaCommand(
        String contentType,
        boolean isPrimary,
        String title,
        String altText
) {
}

