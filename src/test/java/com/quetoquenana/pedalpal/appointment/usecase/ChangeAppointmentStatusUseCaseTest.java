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
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeAppointmentStatusUseCaseTest {

    @Mock
    private AppointmentRepository repository;

    @Mock
    private AppointmentTransitionHandler transitionHandler;

    @Mock
    private AuthenticatedUserPort authenticatedUserPort;

    private final AppointmentMapper mapper = new AppointmentMapper();
    private final Clock clock = Clock.fixed(Instant.parse("2026-03-01T10:00:00Z"), ZoneOffset.UTC);

    private ChangeAppointmentStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ChangeAppointmentStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(transitionHandler),
                clock
        );
    }

    @Test
    void shouldThrowForbiddenWhenNoAuthenticatedUser() {
        UUID appointmentId = UUID.randomUUID();

        when(authenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.empty());

        assertThrows(ForbiddenAccessException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                UUID.randomUUID(),
                "CONFIRMED",
                null,
                null,
                null,
                new BigDecimal("10.00")
        )));
    }

    @Test
    void shouldReturnResultAndServiceOrderNumberWhenAdminConfirms() {
        UUID appointmentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(adminId, UserType.ADMIN);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transitionHandler.supports(AppointmentStatus.REQUESTED, AppointmentStatus.CONFIRMED)).thenReturn(true);
        when(transitionHandler.handle(any(Appointment.class), eq(AppointmentStatus.REQUESTED), any(ChangeAppointmentStatusCommand.class), eq(adminId)))
                .thenReturn("SO-2026-000001");

        ChangeAppointmentStatusResult result = useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "CONFIRMED",
                null,
                null,
                null,
                new BigDecimal("10.00")
        ));

        assertEquals(appointmentId, result.appointmentId());
        assertEquals(AppointmentStatus.REQUESTED, result.fromStatus());
        assertEquals(AppointmentStatus.CONFIRMED, result.toStatus());
        assertEquals(Instant.parse("2026-03-01T10:00:00Z"), result.changedAt());
        assertEquals("SO-2026-000001", result.serviceOrderNumber());
        assertEquals(new BigDecimal("10.00"), result.deposit());
        verify(repository).findByIdAndCustomerId(appointmentId, customerId);
    }

    @Test
    void shouldUseAuthenticatedCustomerIdForLookupWhenCustomerExecutes() {
        UUID appointmentId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(customerId, UserType.CUSTOMER);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChangeAppointmentStatusResult result = useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                UUID.randomUUID(),
                "CANCELED",
                "customer request",
                null,
                null,
                null
        ));

        assertEquals(AppointmentStatus.CANCELED, result.toStatus());
        verify(repository).findByIdAndCustomerId(appointmentId, customerId);
    }

    @ParameterizedTest
    @MethodSource("adminAllowedTransitions")
    void shouldAllowAllDomainValidTransitionsForAdmin(
            AppointmentStatus fromStatus,
            String toStatus,
            String closureReason,
            BigDecimal deposit
    ) {
        UUID appointmentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, fromStatus, new BigDecimal("25.00"));

        mockAuthenticatedUser(adminId, UserType.ADMIN);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChangeAppointmentStatusResult result = useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                toStatus,
                closureReason,
                null,
                null,
                deposit
        ));

        assertEquals(fromStatus, result.fromStatus());
        assertEquals(AppointmentStatus.from(toStatus), result.toStatus());
    }

    @Test
    void shouldFailWhenAppointmentDoesNotExist() {
        UUID appointmentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        mockAuthenticatedUser(adminId, UserType.ADMIN);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "CONFIRMED",
                null,
                null,
                null,
                new BigDecimal("10.00")
        )));
    }

    @Test
    void shouldFailWhenCustomerTriesUnauthorizedTransition() {
        UUID appointmentId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(customerId, UserType.CUSTOMER);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));

        assertThrows(ForbiddenAccessException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "CONFIRMED",
                null,
                null,
                null,
                new BigDecimal("10.00")
        )));
    }

    @Test
    void shouldFailWhenCustomerUsesAuthorizedButDomainInvalidTransition() {
        UUID appointmentId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(customerId, UserType.CUSTOMER);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));

        assertThrows(BusinessException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "DEPOSIT_PAID",
                null,
                null,
                null,
                null
        )));
    }

    @Test
    void shouldFailWhenTransitionIsNotAllowedByDomainEvenForAdmin() {
        UUID appointmentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(adminId, UserType.ADMIN);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));

        assertThrows(BusinessException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "DEPOSIT_PAID",
                null,
                null,
                null,
                null
        )));
    }

    @Test
    void shouldFailWhenClosureReasonMissingForCanceledTransition() {
        UUID appointmentId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(customerId, UserType.CUSTOMER);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));

        assertThrows(BadRequestException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "CANCELED",
                " ",
                null,
                null,
                null
        )));
    }

    @Test
    void shouldFailWhenConfirmingWithoutPositiveDeposit() {
        UUID appointmentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(adminId, UserType.ADMIN);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));

        assertThrows(BadRequestException.class, () -> useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "CONFIRMED",
                null,
                null,
                null,
                BigDecimal.ZERO
        )));
    }

    @Test
    void shouldKeepServiceOrderNumberNullWhenHandlerReturnsBlank() {
        UUID appointmentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(adminId, UserType.ADMIN);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transitionHandler.supports(AppointmentStatus.REQUESTED, AppointmentStatus.CONFIRMED)).thenReturn(true);
        when(transitionHandler.handle(any(Appointment.class), eq(AppointmentStatus.REQUESTED), any(ChangeAppointmentStatusCommand.class), eq(adminId)))
                .thenReturn("  ");

        ChangeAppointmentStatusResult result = useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "CONFIRMED",
                null,
                null,
                null,
                new BigDecimal("10.00")
        ));

        assertNull(result.serviceOrderNumber());
    }

    @Test
    void shouldKeepDepositWhenTransitionDoesNotUpdateDeposit() {
        UUID appointmentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(
                appointmentId,
                customerId,
                AppointmentStatus.CONFIRMED,
                new BigDecimal("15.00")
        );

        mockAuthenticatedUser(adminId, UserType.ADMIN);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChangeAppointmentStatusResult result = useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "BIKE_RECEIVED",
                null,
                null,
                null,
                null
        ));

        assertEquals(new BigDecimal("15.00"), result.deposit());
    }

    @Test
    void shouldPersistDepositWhenTransitionToConfirmed() {
        UUID appointmentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = appointmentWithStatus(appointmentId, customerId, AppointmentStatus.REQUESTED, null);

        mockAuthenticatedUser(adminId, UserType.ADMIN);
        when(repository.findByIdAndCustomerId(appointmentId, customerId)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(new ChangeAppointmentStatusCommand(
                appointmentId,
                customerId,
                "CONFIRMED",
                null,
                null,
                null,
                new BigDecimal("12.50")
        ));

        ArgumentCaptor<Appointment> appointmentCaptor = ArgumentCaptor.forClass(Appointment.class);
        verify(repository, times(1)).save(appointmentCaptor.capture());
        assertEquals(new BigDecimal("12.50"), appointmentCaptor.getValue().getDeposit());
    }

    private static Stream<Arguments> adminAllowedTransitions() {
        return Stream.of(
                Arguments.of(AppointmentStatus.REQUESTED, "CONFIRMED", null, new BigDecimal("10.00")),
                Arguments.of(AppointmentStatus.REQUESTED, "REJECTED", "no slots", null),
                Arguments.of(AppointmentStatus.REQUESTED, "CANCELED", "customer canceled", null),
                Arguments.of(AppointmentStatus.CONFIRMED, "BIKE_RECEIVED", null, null),
                Arguments.of(AppointmentStatus.CONFIRMED, "NO_SHOW", null, null),
                Arguments.of(AppointmentStatus.CONFIRMED, "CANCELED", "customer canceled", null),
                Arguments.of(AppointmentStatus.BIKE_RECEIVED, "IN_PROGRESS", null, null),
                Arguments.of(AppointmentStatus.BIKE_RECEIVED, "CANCELED", "issue detected", null),
                Arguments.of(AppointmentStatus.IN_PROGRESS, "COMPLETED", null, null),
                Arguments.of(AppointmentStatus.IN_PROGRESS, "CANCELED", "cannot continue", null),
                Arguments.of(AppointmentStatus.COMPLETED, "BIKE_PICKED_UP", null, null),
                Arguments.of(AppointmentStatus.CANCELED, "BIKE_PICKED_UP", null, null)
        );
    }

    private Appointment appointmentWithStatus(UUID appointmentId, UUID customerId, AppointmentStatus status, BigDecimal deposit) {
        return Appointment.builder()
                .id(appointmentId)
                .bikeId(UUID.randomUUID())
                .customerId(customerId)
                .storeLocationId(UUID.randomUUID())
                .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                .status(status)
                .deposit(deposit)
                .requestedServices(List.of())
                .build();
    }

    private void mockAuthenticatedUser(UUID userId, UserType userType) {
        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "user", "User", userType)));
    }
}
