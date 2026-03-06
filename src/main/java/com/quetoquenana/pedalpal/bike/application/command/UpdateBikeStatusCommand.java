package com.quetoquenana.pedalpal.bike.application.command;

import java.util.UUID;

public record UpdateBikeStatusCommand(
        UUID bikeId,
        String status
) { }

