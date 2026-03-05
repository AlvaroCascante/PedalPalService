package com.quetoquenana.pedalpal.announcement.application.result;

import com.quetoquenana.pedalpal.announcement.domain.model.AnnouncementStatus;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record AnnouncementResult(
        UUID id,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url,
        AnnouncementStatus status,
        Set<UploadMediaResult> uploadMediaResults
) {
}
