package com.quetoquenana.pedalpal.media.infrastructure.security;

import com.quetoquenana.pedalpal.media.application.port.MediaOwnershipValidationPort;
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
public class MediaOwnershipValidationAdapter implements MediaOwnershipValidationPort {

    private final OwnershipValidator ownershipValidator;

    /**
     * Delegates validation to the security module.
     */
    @Override
    public void validate(MediaReferenceType referenceType, UUID referenceId) {
        ownershipValidator.validate(referenceType, referenceId);
    }
}

