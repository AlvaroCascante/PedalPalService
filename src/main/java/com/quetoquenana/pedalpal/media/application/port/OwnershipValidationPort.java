package com.quetoquenana.pedalpal.media.application.port;

import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;

import java.util.UUID;

/**
 * Port for validating ownership rules in the media module.
 */
public interface OwnershipValidationPort {

    /**
     * Validates whether the authenticated user can manage media for the reference.
     *
     * @param referenceType media reference type
     * @param referenceId reference identifier
     * @param authenticatedUserId authenticated user identifier
     * @param isAdmin whether the authenticated user has admin privileges
     */
    void validate(MediaReferenceType referenceType, UUID referenceId, UUID authenticatedUserId, boolean isAdmin);
}

