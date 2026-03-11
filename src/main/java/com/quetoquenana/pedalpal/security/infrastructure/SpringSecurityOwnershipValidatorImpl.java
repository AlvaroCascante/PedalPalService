package com.quetoquenana.pedalpal.security.infrastructure;

import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.security.application.OwnershipValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SpringSecurityOwnershipValidatorImpl implements OwnershipValidator {

    private final BikeRepository bikeRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuthenticatedUserPort authenticatedUserPort;

    @Override
    public void validate(
            MediaReferenceType ownerType,
            UUID referenceId
    ) {
        AuthenticatedUser currentUser = authenticatedUserPort.getAuthenticatedUser().
                orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        boolean allowed = switch (ownerType) {
            case BIKE -> bikeRepository.existsBydAndOwnerId(referenceId, currentUser.userId());
            case ANNOUNCEMENT -> currentUser.type().equals(UserType.ADMIN);
            default -> false;
        };

        if (!allowed) {
            throw new ForbiddenAccessException("ownership.invalid");
        }
    }
}