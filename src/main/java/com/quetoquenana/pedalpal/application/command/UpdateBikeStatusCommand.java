package com.quetoquenana.pedalpal.application.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateBikeStatusCommand(
        UUID bikeId,
        UUID authenticatedUserId,
        String status
) { }

