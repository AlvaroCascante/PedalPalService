package com.quetoquenana.pedalpal.common.application.command;

import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;

import java.util.Set;
import java.util.UUID;

public record UploadMediaCommand(
        UUID authenticatedUserId,
        boolean isAdmin,
        boolean isPublic,
        UUID referenceId,
        MediaReferenceType referenceType,
        Set<UploadMediaSpecCommand> mediaSpecs
) {}