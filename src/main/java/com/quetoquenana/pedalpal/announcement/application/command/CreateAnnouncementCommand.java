package com.quetoquenana.pedalpal.announcement.application.command;

import java.util.List;
import java.util.UUID;

public record CreateAnnouncementCommand(
        UUID authenticatedUserId,
        boolean isAdmin,
        boolean isPublic,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url,
        List<AnnouncementMediaCommand> mediaFiles
) {
}
