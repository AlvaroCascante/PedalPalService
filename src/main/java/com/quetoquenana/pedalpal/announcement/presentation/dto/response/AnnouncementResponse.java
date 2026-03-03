package com.quetoquenana.pedalpal.announcement.presentation.dto.response;

import com.quetoquenana.pedalpal.media.presentation.dto.response.UploadMediaResponse;

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
        Set<UploadMediaResponse> uploadMediaResponse
) {
}
