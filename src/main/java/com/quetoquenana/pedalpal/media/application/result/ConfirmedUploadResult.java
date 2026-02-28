package com.quetoquenana.pedalpal.media.application.result;

import java.util.UUID;

public record ConfirmedUploadResult(
        UUID mediaId,
        String storageKey,
        String status,
        String cdnUrl
) {}
