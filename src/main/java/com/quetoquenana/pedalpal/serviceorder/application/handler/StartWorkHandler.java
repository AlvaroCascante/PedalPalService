package com.quetoquenana.pedalpal.serviceorder.application.handler;

import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.serviceorder.application.command.ChangeServiceOrderStatusCommand;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetail;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Applies side effects for CREATED -> IN_PROGRESS transitions.
 */
@RequiredArgsConstructor
public class StartWorkHandler implements ServiceOrderTransitionHandler {

    private final Clock clock;

    @Override
    public boolean supports(ServiceOrderStatus fromStatus, ServiceOrderStatus toStatus) {
        return fromStatus == ServiceOrderStatus.CREATED && toStatus == ServiceOrderStatus.IN_PROGRESS;
    }

    @Override
    public void handle(ServiceOrder serviceOrder, ChangeServiceOrderStatusCommand command, AuthenticatedUser user) {
        UUID technicianId = command.technicianId() != null ? command.technicianId() : user.userId();
        Instant now = Instant.now(clock);

        serviceOrder.setStartedAt(now);
        if (command.note() != null && !command.note().isBlank()) {
            serviceOrder.setNotes(command.note().trim());
        }

        List<ServiceOrderDetail> details = serviceOrder.getRequestedServices();
        if (details != null) {
            details.forEach(detail -> {
                detail.setTechnicianId(technicianId);
                if (detail.getStartedAt() == null) {
                    detail.setStartedAt(now);
                }
            });
        }
    }
}
