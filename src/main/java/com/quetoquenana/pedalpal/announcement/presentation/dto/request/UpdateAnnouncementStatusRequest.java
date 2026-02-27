package com.quetoquenana.pedalpal.announcement.presentation.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateAnnouncementStatusRequest(
        @Size(min = 1, message = "{announcement.update.status.blank}")
        String status
) {
}
