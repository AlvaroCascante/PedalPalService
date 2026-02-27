package com.quetoquenana.pedalpal.announcement.presentation.dto.response;

import java.util.UUID;

public record AnnouncementResponse(
        UUID id,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url,
        String status
) {
}
