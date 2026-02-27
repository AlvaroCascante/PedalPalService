package com.quetoquenana.pedalpal.announcement.application.command;

import java.util.UUID;

public record UpdateAnnouncementStatusCommand(
        UUID id,
        String status
) {
}

