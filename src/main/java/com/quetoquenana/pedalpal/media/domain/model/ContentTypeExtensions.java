package com.quetoquenana.pedalpal.media.domain.model;

import com.quetoquenana.pedalpal.common.exception.BadRequestException;

public final class ContentTypeExtensions {

    private ContentTypeExtensions() {}

    public static String extensionFor(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            case "video/mp4" -> "mp4";
            case "video/webm" -> "webm";
            default -> throw new BadRequestException("media.contentType.not.allowed");
        };
    }
}