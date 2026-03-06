package com.quetoquenana.pedalpal.security.application;

import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;

import java.util.UUID;

public interface OwnershipValidator {
    void validate(MediaReferenceType ownerType, UUID referenceId);
}