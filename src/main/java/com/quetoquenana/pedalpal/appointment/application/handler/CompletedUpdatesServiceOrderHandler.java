package com.quetoquenana.pedalpal.appointment.application.handler;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetail;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetailStatus;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Marks service order and details as completed when appointment transitions to COMPLETE.
 */
@RequiredArgsConstructor
public class CompletedUpdatesServiceOrderHandler implements AppointmentTransitionHandler {

    private final ServiceOrderRepository serviceOrderRepository;
    private final Clock clock;

    @Override
    public boolean supports(AppointmentStatus fromStatus, AppointmentStatus toStatus) {
        return fromStatus == AppointmentStatus.IN_PROGRESS && toStatus == AppointmentStatus.COMPLETED;
    }

    @Override
    public String handle(
            Appointment appointment,
            AppointmentStatus fromStatus,
            ChangeAppointmentStatusCommand command,
            UUID actorUserId
    ) {
        ServiceOrder serviceOrder = serviceOrderRepository.findByAppointmentId(appointment.getId())
                .orElseThrow(() -> new RecordNotFoundException("serviceOrder.not.found"));

        UUID technicianId = command.technicianId() != null ? command.technicianId() : actorUserId;
        Instant now = Instant.now(clock);

        serviceOrder.setCompletedAt(now);
        serviceOrder.setStatus(ServiceOrderStatus.COMPLETED);
        if (command.note() != null && !command.note().trim().isEmpty()) {
            serviceOrder.setNotes(command.note().trim());
        }

        List<ServiceOrderDetail> details = serviceOrder.getRequestedServices();
        if (details != null) {
            details.forEach(detail -> {
                detail.setTechnicianId(technicianId);
                detail.setCompletedAt(now);
                detail.setStatus(ServiceOrderDetailStatus.COMPLETED);
            });
        }

        serviceOrderRepository.save(serviceOrder);
        return null;
    }
}
