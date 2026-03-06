package com.quetoquenana.pedalpal.announcement.application.command;

import java.util.List;

public record CreateAnnouncementCommand(
        String title,
        String subTitle,
        String description,
        Integer position,
        String url,
        List<AnnouncementMediaCommand> mediaFiles
) {
}
