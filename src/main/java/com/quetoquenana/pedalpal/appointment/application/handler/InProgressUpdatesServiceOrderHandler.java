package com.quetoquenana.pedalpal.appointment.application.handler;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

/**
 * Sets service order and detail timestamps when an appointment enters IN_PROGRESS.
 */
@RequiredArgsConstructor
public class InProgressUpdatesServiceOrderHandler implements AppointmentTransitionHandler {

    private final ServiceOrderRepository serviceOrderRepository;
    private final Clock clock;

    @Override
    public boolean supports(AppointmentStatus fromStatus, AppointmentStatus toStatus) {
        return fromStatus == AppointmentStatus.BIKE_RECEIVED && toStatus == AppointmentStatus.IN_PROGRESS;
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

        Instant now = Instant.now(clock);

        serviceOrder.setStartedAt(now);
        serviceOrder.setStatus(ServiceOrderStatus.IN_PROGRESS);

        serviceOrderRepository.save(serviceOrder);
        return null;
    }
}
