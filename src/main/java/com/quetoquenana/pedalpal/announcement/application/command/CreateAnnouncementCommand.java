package com.quetoquenana.pedalpal.announcement.application.command;

import java.util.UUID;

public record CreateAnnouncementCommand(
        UUID authenticatedUserId,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url
) {
}
