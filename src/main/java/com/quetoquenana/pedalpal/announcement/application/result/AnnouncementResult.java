package com.quetoquenana.pedalpal.announcement.application.result;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AnnouncementResult(
        UUID id,
        String title,
        String subTitle,
        String description,
        Integer position,
        String url,
        GeneralStatus status
) {
}
