package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.handler.AppointmentTransitionHandler;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.result.ChangeAppointmentStatusResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Single entry point for appointment status transitions.
 */
@Slf4j
@RequiredArgsConstructor
public class ChangeAppointmentStatusUseCase {

    private final AppointmentMapper mapper;
    private final AppointmentRepository repository;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final List<AppointmentTransitionHandler> transitionHandlers;
    private final Clock clock;

    @Transactional
    public ChangeAppointmentStatusResult execute(ChangeAppointmentStatusCommand command) {
        AuthenticatedUser authenticatedUser = authenticatedUserPort.getAuthenticatedUser().
            orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        // If ADMIN, use the customerId from the command, otherwise use the authenticated user's ID
        UUID customerId = authenticatedUser.type().equals(UserType.ADMIN) ? command.customerId() : authenticatedUser.userId();

        Appointment appointment = repository.findByIdAndCustomerId(command.appointmentId(), customerId)
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found", command.appointmentId()));

        AppointmentStatus fromStatus = appointment.getStatus();
        AppointmentStatus toStatus = AppointmentStatus.from(command.toStatus());

        if (!isTransitionAuthorized(authenticatedUser.type(), fromStatus, toStatus)) {
            throw new ForbiddenAccessException("appointment.status.unauthorized");
        }

        appointment.changeStatusTo(
                toStatus,
                command.closureReason(),
                command.deposit()
        );

        repository.save(appointment);

        AtomicReference<String> serviceOrderNumber = new AtomicReference<>();
        transitionHandlers.stream()
                .filter(handler -> handler.supports(fromStatus, toStatus))
                .forEach(handler -> {
                    String handlerResult = handler.handle(appointment, fromStatus, command, authenticatedUser.userId());
                    if (handlerResult != null && !handlerResult.isBlank()) {
                        serviceOrderNumber.set(handlerResult);
                    }
                });

        return mapper.toResult(
                appointment,
                fromStatus,
                Instant.now(clock),
                serviceOrderNumber.get()
        );
    }

    private boolean isTransitionAuthorized(UserType userType, AppointmentStatus fromStatus, AppointmentStatus toStatus) {
        if (userType == null || userType == UserType.UNKNOWN) {
            return false;
        }

        if (userType == UserType.ADMIN) {
            return true;
        }

        if (userType == UserType.CUSTOMER) {
            return (fromStatus == AppointmentStatus.REQUESTED && toStatus == AppointmentStatus.CANCELED)
                    || (fromStatus == AppointmentStatus.REQUESTED && toStatus == AppointmentStatus.DEPOSIT_PAID);
        }

        if (userType == UserType.TECHNICIAN) {
            return (fromStatus == AppointmentStatus.BIKE_RECEIVED && toStatus == AppointmentStatus.IN_PROGRESS)
                    || (fromStatus == AppointmentStatus.IN_PROGRESS && toStatus == AppointmentStatus.COMPLETED)
                    || (fromStatus == AppointmentStatus.COMPLETED && toStatus == AppointmentStatus.BIKE_PICKED_UP);
        }
        return false;
    }
}
