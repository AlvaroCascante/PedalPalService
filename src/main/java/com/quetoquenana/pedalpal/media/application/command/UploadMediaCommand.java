package com.quetoquenana.pedalpal.media.application.command;

import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UploadMediaCommand(
        boolean isPublic,
        UUID referenceId,
        MediaReferenceType referenceType,
        List<UploadMediaSpecCommand> mediaSpecs
) {}