package com.quetoquenana.pedalpal.serviceorder.usecase;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceorder.application.command.ChangeServiceOrderStatusCommand;
import com.quetoquenana.pedalpal.serviceorder.application.handler.CompleteWorkHandler;
import com.quetoquenana.pedalpal.serviceorder.application.handler.ServiceOrderTransitionHandler;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.application.port.CompleteAppointmentPort;
import com.quetoquenana.pedalpal.serviceorder.application.result.ChangeServiceOrderStatusResult;
import com.quetoquenana.pedalpal.serviceorder.application.usecase.ChangeServiceOrderStatusUseCase;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetail;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetailStatus;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeServiceOrderStatusUseCaseTest {

    @Mock
    private ServiceOrderRepository repository;

    @Mock
    private AuthenticatedUserPort authenticatedUserPort;

    @Mock
    private ServiceOrderTransitionHandler transitionHandler;

    private final ServiceOrderMapper mapper = new ServiceOrderMapper();

    private ChangeServiceOrderStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ChangeServiceOrderStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(transitionHandler)
        );
    }

    @Test
    void shouldThrowForbiddenWhenNoAuthenticatedUser() {
        ServiceOrder serviceOrder = serviceOrderWithStatus(UUID.randomUUID(), ServiceOrderStatus.CREATED);
        when(authenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.empty());

        when(repository.getById(any())).thenReturn(Optional.ofNullable(serviceOrder));
        assertThrows(ForbiddenAccessException.class, () -> useCase.execute(new ChangeServiceOrderStatusCommand(
                UUID.randomUUID(),
                ServiceOrderStatus.IN_PROGRESS.name(),
                null,
                null
        )));
    }

    @Test
    void shouldThrowNotFoundWhenServiceOrderDoesNotExist() {
        UUID serviceOrderId = UUID.randomUUID();
        when(repository.getById(serviceOrderId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> useCase.execute(new ChangeServiceOrderStatusCommand(
                serviceOrderId,
                ServiceOrderStatus.IN_PROGRESS.name(),
                null,
                null
        )));
    }

    @ParameterizedTest
    @MethodSource("validTransitions")
    void shouldAllowAllValidTransitions(ServiceOrderStatus fromStatus, ServiceOrderStatus toStatus) {
        UUID serviceOrderId = UUID.randomUUID();
        ServiceOrder serviceOrder = serviceOrderWithStatus(serviceOrderId, fromStatus);

        mockAuthenticatedUser(UUID.randomUUID());
        when(repository.getById(serviceOrderId)).thenReturn(Optional.of(serviceOrder));
        when(repository.save(any(ServiceOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChangeServiceOrderStatusResult result = useCase.execute(new ChangeServiceOrderStatusCommand(
                serviceOrderId,
                toStatus.name(),
                null,
                null
        ));

        assertEquals(fromStatus, result.fromStatus());
        assertEquals(toStatus, result.toStatus());
    }

    @Test
    void shouldThrowBusinessExceptionWhenTransitionIsInvalid() {
        UUID serviceOrderId = UUID.randomUUID();
        ServiceOrder serviceOrder = serviceOrderWithStatus(serviceOrderId, ServiceOrderStatus.CREATED);

        mockAuthenticatedUser(UUID.randomUUID());
        when(repository.getById(serviceOrderId)).thenReturn(Optional.of(serviceOrder));

        assertThrows(BusinessException.class, () -> useCase.execute(new ChangeServiceOrderStatusCommand(
                serviceOrderId,
                ServiceOrderStatus.COMPLETED.name(),
                null,
                null
        )));
    }

    @Test
    void shouldUpdateAppointmentOnCompletionSideEffect() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID appointmentId = UUID.randomUUID();
        UUID actorId = UUID.randomUUID();
        Clock fixedClock = Clock.fixed(Instant.parse("2026-03-06T16:00:00Z"), ZoneOffset.UTC);

        CompleteAppointmentPort completeAppointmentPort = org.mockito.Mockito.mock(CompleteAppointmentPort.class);
        CompleteWorkHandler completeWorkHandler = new CompleteWorkHandler(completeAppointmentPort, fixedClock);

        ChangeServiceOrderStatusUseCase useCaseWithRealCompletionHandler = new ChangeServiceOrderStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(completeWorkHandler)
        );

        ServiceOrderDetail detail = ServiceOrderDetail.builder()
                .status(ServiceOrderDetailStatus.IN_PROGRESS)
                .build();

        ServiceOrder serviceOrder = ServiceOrder.builder()
                .id(serviceOrderId)
                .appointmentId(appointmentId)
                .status(ServiceOrderStatus.IN_PROGRESS)
                .requestedServices(List.of(detail))
                .build();

        mockAuthenticatedUser(actorId);
        when(repository.getById(serviceOrderId)).thenReturn(Optional.of(serviceOrder));
        when(repository.save(any(ServiceOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCaseWithRealCompletionHandler.execute(new ChangeServiceOrderStatusCommand(
                serviceOrderId,
                ServiceOrderStatus.COMPLETED.name(),
                null,
                "completed"
        ));

        verify(completeAppointmentPort).completeAppointment(appointmentId);

        ArgumentCaptor<ServiceOrder> captor = ArgumentCaptor.forClass(ServiceOrder.class);
        verify(repository, times(1)).save(captor.capture());
        assertEquals(ServiceOrderStatus.COMPLETED, captor.getValue().getStatus());
    }

    @Test
    void shouldCallOnlyHandlersThatSupportTransition() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID actorId = UUID.randomUUID();
        ServiceOrder serviceOrder = serviceOrderWithStatus(serviceOrderId, ServiceOrderStatus.CREATED);

        mockAuthenticatedUser(actorId);
        when(repository.getById(serviceOrderId)).thenReturn(Optional.of(serviceOrder));
        when(repository.save(any(ServiceOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transitionHandler.supports(ServiceOrderStatus.CREATED, ServiceOrderStatus.IN_PROGRESS)).thenReturn(true);

        useCase.execute(new ChangeServiceOrderStatusCommand(
                serviceOrderId,
                ServiceOrderStatus.IN_PROGRESS.name(),
                null,
                null
        ));

        verify(transitionHandler).handle(any(ServiceOrder.class), any(ChangeServiceOrderStatusCommand.class), any(AuthenticatedUser.class));
    }

    @Test
    void shouldNotCallHandlersWhenTransitionIsInvalid() {
        UUID serviceOrderId = UUID.randomUUID();
        ServiceOrder serviceOrder = serviceOrderWithStatus(serviceOrderId, ServiceOrderStatus.CREATED);

        mockAuthenticatedUser(UUID.randomUUID());
        when(repository.getById(serviceOrderId)).thenReturn(Optional.of(serviceOrder));

        assertThrows(BusinessException.class, () -> useCase.execute(new ChangeServiceOrderStatusCommand(
                serviceOrderId,
                ServiceOrderStatus.COMPLETED.name(),
                null,
                null
        )));

        verify(transitionHandler, never()).handle(any(ServiceOrder.class), any(ChangeServiceOrderStatusCommand.class), any(AuthenticatedUser.class));
    }

    private static Stream<Arguments> validTransitions() {
        return Stream.of(
                Arguments.of(ServiceOrderStatus.CREATED, ServiceOrderStatus.IN_PROGRESS),
                Arguments.of(ServiceOrderStatus.CREATED, ServiceOrderStatus.AWAITING_PARTS),
                Arguments.of(ServiceOrderStatus.CREATED, ServiceOrderStatus.CANCELED),
                Arguments.of(ServiceOrderStatus.IN_PROGRESS, ServiceOrderStatus.AWAITING_PARTS),
                Arguments.of(ServiceOrderStatus.IN_PROGRESS, ServiceOrderStatus.COMPLETED),
                Arguments.of(ServiceOrderStatus.IN_PROGRESS, ServiceOrderStatus.CANCELED),
                Arguments.of(ServiceOrderStatus.AWAITING_PARTS, ServiceOrderStatus.IN_PROGRESS),
                Arguments.of(ServiceOrderStatus.AWAITING_PARTS, ServiceOrderStatus.CANCELED)
        );
    }

    private ServiceOrder serviceOrderWithStatus(UUID serviceOrderId, ServiceOrderStatus status) {
        return ServiceOrder.builder()
                .id(serviceOrderId)
                .appointmentId(UUID.randomUUID())
                .status(status)
                .requestedServices(List.of())
                .build();
    }

    private void mockAuthenticatedUser(UUID userId) {
        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "tech", "Tech", UserType.TECHNICIAN)));
    }
}
