package com.quetoquenana.pedalpal.media.application.command;

import java.util.UUID;

public record ConfirmUploadCommand(
        UUID mediaId,
        UUID authenticatedUserId
) {}