package com.quetoquenana.pedalpal.announcement.presentation.dto.response;

import com.quetoquenana.pedalpal.common.presentation.dto.response.MediaUrlResponse;

import java.util.Set;
import java.util.UUID;

public record AnnouncementResponse(
        UUID id,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url,
        String status,
        Set<MediaUrlResponse> mediaUrlResponse
) {
}
