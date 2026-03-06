package com.quetoquenana.pedalpal.announcement.application.command;

import java.util.UUID;

public record UpdateAnnouncementCommand(
        UUID id,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url
) {
    public UpdateAnnouncementCommand(UUID id) {
        this(id, null, null, null, null, null);
    }
}
