package com.quetoquenana.pedalpal.bike.application.command;

import java.util.UUID;

public record UpdateBikeComponentStatusCommand(
        UUID bikeId,
        UUID componentId,
        String status
) { }

