package com.quetoquenana.pedalpal.serviceorder.usecase;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceorder.application.command.AddServiceOrderCommentCommand;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderCommentResult;
import com.quetoquenana.pedalpal.serviceorder.application.usecase.AddServiceOrderCommentUseCase;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderComment;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderCommentRepository;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddServiceOrderCommentUseCaseTest {

    private static final Instant NOW = Instant.parse("2026-03-06T12:00:00Z");

    @Mock
    AuthenticatedUserPort authenticatedUserPort;

    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @Mock
    ServiceOrderCommentRepository commentRepository;

    @Mock
    ServiceOrderMapper mapper;

    AddServiceOrderCommentUseCase useCase;

    Clock clock;

    @Captor
    ArgumentCaptor<ServiceOrderComment> commentCaptor;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(NOW, ZoneOffset.UTC);
        useCase = new AddServiceOrderCommentUseCase(
                authenticatedUserPort,
                serviceOrderRepository,
                commentRepository,
                mapper,
                clock
        );
    }

    @Test
    void shouldAddCommentWithDefaults() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        mockAuthenticatedUser(userId, UserType.TECHNICIAN);
        mockServiceOrderExists(serviceOrderId);

        AddServiceOrderCommentCommand command = new AddServiceOrderCommentCommand(
                serviceOrderId,
                "  Work completed successfully  ",
                null
        );

        when(commentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toModel(any(), any(), any())).thenAnswer(invocation -> {
            AddServiceOrderCommentCommand cmd = invocation.getArgument(0, AddServiceOrderCommentCommand.class);
            AuthenticatedUser user = invocation.getArgument(1, AuthenticatedUser.class);
            Instant now = invocation.getArgument(2, Instant.class);
            return ServiceOrderComment.builder()
                    .id(UUID.randomUUID())
                    .serviceOrderId(cmd.serviceOrderId())
                    .comment(cmd.comment())
                    .customerVisible(cmd.customerVisible())
                    .createdByType(user.type())
                    .createdAt(now)
                    .createdBy(user.userId())
                    .build();
        });
        when(mapper.toResult(any(ServiceOrderComment.class))).thenAnswer(invocation -> {
            ServiceOrderComment saved = invocation.getArgument(0, ServiceOrderComment.class);
            return new ServiceOrderCommentResult(
                    saved.getId(),
                    saved.getServiceOrderId(),
                    saved.getComment(),
                    saved.getCustomerVisible(),
                    saved.getCreatedByType(),
                    saved.getCreatedAt(),
                    saved.getCreatedBy()
            );
        });

        ServiceOrderCommentResult result = useCase.execute(command);

        verify(commentRepository).save(commentCaptor.capture());
        ServiceOrderComment saved = commentCaptor.getValue();

        assertNotNull(saved.getId());
        assertEquals(serviceOrderId, saved.getServiceOrderId());
        assertEquals("  Work completed successfully  ", saved.getComment());
        assertNull(saved.getCustomerVisible());
        assertEquals(NOW, saved.getCreatedAt());
        assertEquals(userId, saved.getCreatedBy());
        assertEquals("TECHNICIAN", saved.getCreatedByType().name());

        assertEquals(saved.getId(), result.id());
        assertEquals(UserType.TECHNICIAN, result.createdByType());
    }

    @Test
    void shouldMapUnknownUserTypeToSystem() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        mockAuthenticatedUser(userId, UserType.UNKNOWN);
        mockServiceOrderExists(serviceOrderId);

        AddServiceOrderCommentCommand command = new AddServiceOrderCommentCommand(
                serviceOrderId,
                "Customer requested callback",
                Boolean.TRUE
        );

        when(commentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toModel(any(), any(), any())).thenAnswer(invocation -> {
            AddServiceOrderCommentCommand cmd = invocation.getArgument(0, AddServiceOrderCommentCommand.class);
            AuthenticatedUser user = invocation.getArgument(1, AuthenticatedUser.class);
            Instant now = invocation.getArgument(2, Instant.class);
            return ServiceOrderComment.builder()
                    .id(UUID.randomUUID())
                    .serviceOrderId(cmd.serviceOrderId())
                    .comment(cmd.comment())
                    .customerVisible(cmd.customerVisible())
                    .createdByType(UserType.SYSTEM)
                    .createdAt(now)
                    .createdBy(user.userId())
                    .build();
        });
        when(mapper.toResult(any(ServiceOrderComment.class))).thenAnswer(invocation -> {
            ServiceOrderComment saved = invocation.getArgument(0, ServiceOrderComment.class);
            return new ServiceOrderCommentResult(
                    saved.getId(),
                    saved.getServiceOrderId(),
                    saved.getComment(),
                    saved.getCustomerVisible(),
                    saved.getCreatedByType(),
                    saved.getCreatedAt(),
                    saved.getCreatedBy()
            );
        });

        ServiceOrderCommentResult result = useCase.execute(command);

        assertEquals(UserType.SYSTEM, result.createdByType());
    }

    @Test
    void shouldThrowWhenCommentIsBlank() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        mockAuthenticatedUser(userId, UserType.ADMIN);
        mockServiceOrderExists(serviceOrderId);

        AddServiceOrderCommentCommand command = new AddServiceOrderCommentCommand(
                serviceOrderId,
                "   ",
                Boolean.TRUE
        );

        assertThrows(BadRequestException.class, () -> useCase.execute(command));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenCommentExceedsMaxLength() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        mockAuthenticatedUser(userId, UserType.ADMIN);
        mockServiceOrderExists(serviceOrderId);

        String comment = "x".repeat(1001);
        AddServiceOrderCommentCommand command = new AddServiceOrderCommentCommand(
                serviceOrderId,
                comment,
                Boolean.TRUE
        );

        assertThrows(BadRequestException.class, () -> useCase.execute(command));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenNotAuthenticated() {
        UUID serviceOrderId = UUID.randomUUID();

        when(authenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.empty());

        AddServiceOrderCommentCommand command = new AddServiceOrderCommentCommand(
                serviceOrderId,
                "Comment",
                Boolean.TRUE
        );

        assertThrows(ForbiddenAccessException.class, () -> useCase.execute(command));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenServiceOrderMissing() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        mockAuthenticatedUser(userId, UserType.CUSTOMER);
        when(serviceOrderRepository.getById(serviceOrderId)).thenReturn(Optional.empty());

        AddServiceOrderCommentCommand command = new AddServiceOrderCommentCommand(
                serviceOrderId,
                "Comment",
                Boolean.TRUE
        );

        assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
        verify(commentRepository, never()).save(any());
    }

    private void mockAuthenticatedUser(UUID userId, UserType userType) {
        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(userId, "test-user", "Test User", userType)));
    }

    private void mockServiceOrderExists(UUID serviceOrderId) {
        ServiceOrder serviceOrder = ServiceOrder.builder()
                .id(serviceOrderId)
                .status(ServiceOrderStatus.CREATED)
                .build();
        when(serviceOrderRepository.getById(serviceOrderId)).thenReturn(Optional.of(serviceOrder));
    }
}
