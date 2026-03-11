package com.quetoquenana.pedalpal.announcement.application.command;

public record AnnouncementMediaCommand(
        String contentType,
        boolean isPrimary,
        String name,
        String altText
) {}
