package com.quetoquenana.pedalpal.announcement.application.result;

import com.quetoquenana.pedalpal.announcement.domain.model.AnnouncementStatus;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;

import java.util.List;
import java.util.UUID;

public record AnnouncementResult(
        UUID id,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url,
        AnnouncementStatus status,
        List<MediaResult> mediaResults
) {
}
