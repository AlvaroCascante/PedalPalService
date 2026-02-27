package com.quetoquenana.pedalpal.announcement.application.command;

import java.util.UUID;

public record UpdateAnnouncementCommand(
        UUID id,
        UUID authenticatedUserId,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url
) {
}
