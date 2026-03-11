package com.quetoquenana.pedalpal.media.application.port;

import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;

import java.util.UUID;

/**
 * Port for validating ownership rules in the media module.
 */
public interface MediaOwnershipValidationPort {

    /**
     * Validates whether the authenticated user can manage media for the reference.
     *
     * @param referenceType media reference type
     * @param referenceId reference identifier
     */
    void validate(MediaReferenceType referenceType, UUID referenceId);
}

