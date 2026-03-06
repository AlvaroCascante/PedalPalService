package com.quetoquenana.pedalpal.appointment.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.handler.AppointmentTransitionHandler;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.result.ChangeAppointmentStatusResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.ChangeAppointmentStatusUseCase;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeAppointmentStatusUseCaseTest {

    @Mock
    private AppointmentRepository repository;

    @Mock
    private AppointmentMapper mapper;

    @Mock
    private AppointmentTransitionHandler transitionHandler;

    @Mock
    private AuthenticatedUserPort authenticatedUserPort;

    private final Clock clock = Clock.fixed(Instant.parse("2026-03-01T10:00:00Z"), ZoneOffset.UTC);

    @InjectMocks
    private ChangeAppointmentStatusUseCase useCase;

    @Test
    void shouldChangeStatusAndReturnServiceOrderNumber_whenAdmin() {
        UUID appointmentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .bikeId(UUID.randomUUID())
                .storeLocationId(UUID.randomUUID())
                .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                .status(AppointmentStatus.REQUESTED)
                .requestedServices(List.of())
                .build();

        useCase = new ChangeAppointmentStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(transitionHandler),
                clock
        );

        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "admin", "Admin", UserType.ADMIN)));
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transitionHandler.supports(AppointmentStatus.REQUESTED, AppointmentStatus.CONFIRMED)).thenReturn(true);
        when(transitionHandler.handle(
                any(Appointment.class),
                eq(AppointmentStatus.REQUESTED),
                any(ChangeAppointmentStatusCommand.class),
                eq(userId)
        )).thenReturn("SO-2026-000001");

        ChangeAppointmentStatusResult result = useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                "CONFIRMED",
                null,
                null,
                null
        ));

        assertEquals(AppointmentStatus.REQUESTED, result.fromStatus());
        assertEquals(AppointmentStatus.CONFIRMED, result.toStatus());
        assertEquals("SO-2026-000001", result.serviceOrderNumber());
        assertEquals(Instant.parse("2026-03-01T10:00:00Z"), result.changedAt());
        verify(repository).save(any(Appointment.class));
    }

    @Test
    void shouldFailWhenAppointmentDoesNotExist() {
        UUID appointmentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        useCase = new ChangeAppointmentStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(transitionHandler),
                clock
        );

        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "admin", "Admin", UserType.ADMIN)));
        when(repository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                "CONFIRMED",
                null,
                null,
                null
        )));
    }

    @Test
    void shouldFailWhenClosureReasonMissingForCanceledByCustomer() {
        UUID appointmentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .status(AppointmentStatus.REQUESTED)
                .requestedServices(List.of())
                .build();

        useCase = new ChangeAppointmentStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(),
                clock
        );

        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "customer", "Customer", UserType.CUSTOMER)));
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThrows(BadRequestException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                "CANCELED",
                " ",
                null,
                null
        )));
    }

    @Test
    void shouldFailWhenCustomerTriesToConfirm() {
        UUID appointmentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .status(AppointmentStatus.REQUESTED)
                .requestedServices(List.of())
                .build();

        useCase = new ChangeAppointmentStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(),
                clock
        );

        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "customer", "Customer", UserType.CUSTOMER)));
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThrows(ForbiddenAccessException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                "CONFIRMED",
                null,
                null,
                null
        )));
    }

    @Test
    void shouldAllowTechnicianToMoveToInProgress() {
        UUID appointmentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .status(AppointmentStatus.BIKE_RECEIVED)
                .requestedServices(List.of())
                .build();

        useCase = new ChangeAppointmentStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(),
                clock
        );

        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "tech", "Technician", UserType.TECHNICIAN)));
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));

        ChangeAppointmentStatusResult result = useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                "IN_PROGRESS",
                null,
                null,
                null
        ));

        assertEquals(AppointmentStatus.BIKE_RECEIVED, result.fromStatus());
        assertEquals(AppointmentStatus.IN_PROGRESS, result.toStatus());
    }

    @Test
    void shouldFailWhenTechnicianTriesToCancel() {
        UUID appointmentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .status(AppointmentStatus.IN_PROGRESS)
                .requestedServices(List.of())
                .build();

        useCase = new ChangeAppointmentStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(),
                clock
        );

        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "tech", "Technician", UserType.TECHNICIAN)));
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThrows(ForbiddenAccessException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                "CANCELED",
                "no parts",
                null,
                null
        )));
    }
}
