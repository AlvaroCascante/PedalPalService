package com.quetoquenana.pedalpal.announcement.application.command;

public record AnnouncementMediaCommand(
        String contentType,
        String mediaType,
        boolean isPrimary,
        String name,
        String altText
) {}
