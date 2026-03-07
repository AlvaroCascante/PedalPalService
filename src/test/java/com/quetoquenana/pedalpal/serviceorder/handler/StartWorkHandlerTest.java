package com.quetoquenana.pedalpal.serviceorder.handler;

import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.serviceorder.application.command.ChangeServiceOrderStatusCommand;
import com.quetoquenana.pedalpal.serviceorder.application.handler.StartWorkHandler;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetail;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartWorkHandlerTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-03-06T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void shouldSupportCreatedToInProgressOnly() {
        StartWorkHandler handler = new StartWorkHandler(clock);

        assertTrue(handler.supports(ServiceOrderStatus.CREATED, ServiceOrderStatus.IN_PROGRESS));
        assertFalse(handler.supports(ServiceOrderStatus.CREATED, ServiceOrderStatus.COMPLETED));
        assertFalse(handler.supports(ServiceOrderStatus.AWAITING_PARTS, ServiceOrderStatus.IN_PROGRESS));
    }

    @Test
    void shouldSetStartedAtAndUseTechnicianFromCommandWhenPresent() {
        StartWorkHandler handler = new StartWorkHandler(clock);
        UUID commandTechnicianId = UUID.randomUUID();
        UUID actorId = UUID.randomUUID();
        Instant existingDetailStartedAt = Instant.parse("2026-03-05T12:00:00Z");

        ServiceOrderDetail detailWithoutStart = ServiceOrderDetail.builder().build();
        ServiceOrderDetail detailWithStart = ServiceOrderDetail.builder().startedAt(existingDetailStartedAt).build();

        ServiceOrder serviceOrder = ServiceOrder.builder()
                .status(ServiceOrderStatus.IN_PROGRESS)
                .requestedServices(List.of(detailWithoutStart, detailWithStart))
                .build();

        ChangeServiceOrderStatusCommand command = new ChangeServiceOrderStatusCommand(
                UUID.randomUUID(),
                ServiceOrderStatus.IN_PROGRESS.name(),
                commandTechnicianId,
                "  begin now  "
        );

        AuthenticatedUser user = new AuthenticatedUser(actorId, "tech", "Tech", UserType.TECHNICIAN);

        handler.handle(serviceOrder, command, user);

        Instant expected = Instant.parse("2026-03-06T12:00:00Z");
        assertEquals(expected, serviceOrder.getStartedAt());
        assertEquals("begin now", serviceOrder.getNotes());

        assertEquals(commandTechnicianId, detailWithoutStart.getTechnicianId());
        assertEquals(expected, detailWithoutStart.getStartedAt());

        assertEquals(commandTechnicianId, detailWithStart.getTechnicianId());
        assertEquals(existingDetailStartedAt, detailWithStart.getStartedAt());
    }

    @Test
    void shouldFallbackToAuthenticatedUserWhenTechnicianIsMissing() {
        StartWorkHandler handler = new StartWorkHandler(clock);
        UUID actorId = UUID.randomUUID();

        ServiceOrderDetail detail = ServiceOrderDetail.builder().build();
        ServiceOrder serviceOrder = ServiceOrder.builder()
                .status(ServiceOrderStatus.IN_PROGRESS)
                .requestedServices(List.of(detail))
                .build();

        ChangeServiceOrderStatusCommand command = new ChangeServiceOrderStatusCommand(
                UUID.randomUUID(),
                ServiceOrderStatus.IN_PROGRESS.name(),
                null,
                null
        );

        AuthenticatedUser user = new AuthenticatedUser(actorId, "tech", "Tech", UserType.TECHNICIAN);

        handler.handle(serviceOrder, command, user);

        assertEquals(actorId, detail.getTechnicianId());
        assertNotNull(detail.getStartedAt());
    }
}

