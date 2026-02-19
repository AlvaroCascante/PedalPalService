package com.quetoquenana.pedalpal.application.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateBikeComponentStatusCommand(
        UUID bikeId,
        UUID componentId,
        UUID authenticatedUserId,
        String status
) { }

