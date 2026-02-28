package com.quetoquenana.pedalpal.security.infrastructure;

import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.security.application.OwnershipValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SpringSecurityOwnershipValidatorImpl implements OwnershipValidator {

    private final BikeRepository bikeRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public void validate(
            MediaReferenceType ownerType,
            UUID ownerId,
            UUID authenticatedUserId,
            Boolean isAdmin
    ) {

        boolean allowed = switch (ownerType) {
            case BIKE -> bikeRepository.existsBydAndOwnerId(ownerId, authenticatedUserId);
            case ANNOUNCEMENT -> isAdmin;
            default -> false;
        };

        if (!allowed) {
            throw new ForbiddenAccessException("ownership.invalid");
        }
    }
}