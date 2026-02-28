package com.quetoquenana.pedalpal.media.presentation.dto.response;

import java.util.UUID;

public record ConfirmedUploadResponse(
        UUID mediaId,
        String storageKey,
        String status,
        String cdnUrl
) {}