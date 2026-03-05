package com.quetoquenana.pedalpal.media.infrastructure.security;

import com.quetoquenana.pedalpal.media.application.port.OwnershipValidationPort;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.security.application.OwnershipValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Infrastructure adapter that delegates media ownership checks to the security module.
 */
@Component
@RequiredArgsConstructor
public class OwnershipValidationAdapter implements OwnershipValidationPort {

    private final OwnershipValidator ownershipValidator;

    /**
     * Delegates validation to the security module.
     */
    @Override
    public void validate(MediaReferenceType referenceType, UUID referenceId, UUID authenticatedUserId, boolean isAdmin) {
        ownershipValidator.validate(referenceType, referenceId, authenticatedUserId, isAdmin);
    }
}

