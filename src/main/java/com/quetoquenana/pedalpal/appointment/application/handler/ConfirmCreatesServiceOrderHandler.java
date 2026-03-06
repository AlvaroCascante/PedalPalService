package com.quetoquenana.pedalpal.appointment.application.handler;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.serviceOrder.application.util.ServiceOrderNumberGenerator;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

/**
 * Creates a service order when an appointment is confirmed.
 */
@RequiredArgsConstructor
public class ConfirmCreatesServiceOrderHandler implements AppointmentTransitionHandler {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderNumberGenerator numberGenerator;
    private final Clock clock;

    @Override
    public boolean supports(AppointmentStatus fromStatus, AppointmentStatus toStatus) {
        return fromStatus == AppointmentStatus.REQUESTED && toStatus == AppointmentStatus.CONFIRMED;
    }

    @Override
    public String handle(
            Appointment appointment,
            AppointmentStatus fromStatus,
            ChangeAppointmentStatusCommand command,
            UUID actorUserId
    ) {
        Optional<ServiceOrder> existing = serviceOrderRepository.findByAppointmentId(appointment.getId());
        if (existing.isPresent()) {
            return existing.get().getOrderNumber();
        }

        ServiceOrder serviceOrder = ServiceOrder.createFromAppointment(appointment);
        long sequence = serviceOrderRepository.nextOrderSequence();
        serviceOrder.setOrderNumber(numberGenerator.generate(sequence, clock));

        ServiceOrder saved = serviceOrderRepository.save(serviceOrder);
        return saved.getOrderNumber();
    }
}
