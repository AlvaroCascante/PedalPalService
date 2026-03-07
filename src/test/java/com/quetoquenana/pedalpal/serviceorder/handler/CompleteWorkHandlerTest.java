package com.quetoquenana.pedalpal.serviceorder.handler;

import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.serviceorder.application.command.ChangeServiceOrderStatusCommand;
import com.quetoquenana.pedalpal.serviceorder.application.handler.CompleteWorkHandler;
import com.quetoquenana.pedalpal.serviceorder.application.port.CompleteAppointmentPort;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetail;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetailStatus;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CompleteWorkHandlerTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-03-06T15:00:00Z"), ZoneOffset.UTC);

    @Test
    void shouldSupportInProgressToCompletedOnly() {
        CompleteAppointmentPort completeAppointmentPort = mock(CompleteAppointmentPort.class);
        CompleteWorkHandler handler = new CompleteWorkHandler(completeAppointmentPort, clock);

        assertTrue(handler.supports(ServiceOrderStatus.IN_PROGRESS, ServiceOrderStatus.COMPLETED));
        assertFalse(handler.supports(ServiceOrderStatus.CREATED, ServiceOrderStatus.COMPLETED));
        assertFalse(handler.supports(ServiceOrderStatus.IN_PROGRESS, ServiceOrderStatus.CANCELED));
    }

    @Test
    void shouldUpdateDetailsAndTriggerAppointmentCompletion() {
        CompleteAppointmentPort completeAppointmentPort = mock(CompleteAppointmentPort.class);
        CompleteWorkHandler handler = new CompleteWorkHandler(completeAppointmentPort, clock);

        UUID appointmentId = UUID.randomUUID();
        UUID technicianId = UUID.randomUUID();
        UUID actorId = UUID.randomUUID();

        ServiceOrderDetail detail = ServiceOrderDetail.builder()
                .status(ServiceOrderDetailStatus.IN_PROGRESS)
                .build();

        ServiceOrder serviceOrder = ServiceOrder.builder()
                .appointmentId(appointmentId)
                .status(ServiceOrderStatus.COMPLETED)
                .requestedServices(List.of(detail))
                .build();

        ChangeServiceOrderStatusCommand command = new ChangeServiceOrderStatusCommand(
                UUID.randomUUID(),
                ServiceOrderStatus.COMPLETED.name(),
                technicianId,
                "  done  "
        );

        AuthenticatedUser user = new AuthenticatedUser(actorId, "tech", "Tech", UserType.TECHNICIAN);

        handler.handle(serviceOrder, command, user);

        Instant expected = Instant.parse("2026-03-06T15:00:00Z");
        assertEquals(expected, serviceOrder.getCompletedAt());
        assertEquals("done", serviceOrder.getNotes());

        assertEquals(technicianId, detail.getTechnicianId());
        assertEquals(expected, detail.getCompletedAt());
        assertEquals(ServiceOrderDetailStatus.COMPLETED, detail.getStatus());

        verify(completeAppointmentPort).completeAppointment(appointmentId);
    }

    @Test
    void shouldFallbackToActorWhenTechnicianIsMissing() {
        CompleteAppointmentPort completeAppointmentPort = mock(CompleteAppointmentPort.class);
        CompleteWorkHandler handler = new CompleteWorkHandler(completeAppointmentPort, clock);

        UUID appointmentId = UUID.randomUUID();
        UUID actorId = UUID.randomUUID();

        ServiceOrderDetail detail = ServiceOrderDetail.builder().build();
        ServiceOrder serviceOrder = ServiceOrder.builder()
                .appointmentId(appointmentId)
                .requestedServices(List.of(detail))
                .build();

        ChangeServiceOrderStatusCommand command = new ChangeServiceOrderStatusCommand(
                UUID.randomUUID(),
                ServiceOrderStatus.COMPLETED.name(),
                null,
                null
        );

        AuthenticatedUser user = new AuthenticatedUser(actorId, "tech", "Tech", UserType.TECHNICIAN);

        handler.handle(serviceOrder, command, user);

        assertEquals(actorId, detail.getTechnicianId());
        verify(completeAppointmentPort).completeAppointment(appointmentId);
    }
}

