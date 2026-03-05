package com.quetoquenana.pedalpal.media.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

/**
 * Supported content types for media uploads.
 */
@Getter
public enum MediaContentType {
    IMAGE_JPEG("image/jpeg", "jpg"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_WEBP("image/webp", "webp"),
    VIDEO_MP4("video/mp4", "mp4"),
    VIDEO_WEBM("video/webm", "webm"),
    UNKNOWN("", "");

    private final String contentType;
    private final String extension;

    MediaContentType(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    /**
     * Resolves the file extension for a given content type.
     */
    public static String extensionFor(String contentType) {
        return fromContentType(contentType).getExtension();
    }

    /**
     * Resolves the enum by content type value.
     */
    public static MediaContentType fromContentType(String contentType) {
        String normalized = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(type -> type.contentType.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}