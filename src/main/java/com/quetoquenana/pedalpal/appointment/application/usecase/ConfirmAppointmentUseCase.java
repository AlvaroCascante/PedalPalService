package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.result.ConfirmAppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceOrder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * Use case for confirming an appointment.
 * It retrieves the appointment, updates its status to CONFIRMED
 * Creates a service order based on the appointment details.
 * It also handles exceptions and logs errors.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmAppointmentUseCase {

    private final AppointmentMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private final ServiceOrderRepository serviceOrderRepository;

    @Transactional
    public ConfirmAppointmentResult execute(UpdateAppointmentStatusCommand command) {
        Appointment appointment = appointmentRepository.getById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found"));
        try {
            appointment.confirm();
            appointmentRepository.save(appointment);

            ServiceOrder serviceOrder = ServiceOrder.createFromAppointment(appointment);
            serviceOrder = serviceOrderRepository.save(serviceOrder);

            return new ConfirmAppointmentResult(
                    mapper.toResult(appointment),
                    serviceOrderMapper.toResult(serviceOrder)
            );
        } catch (RuntimeException ex) {
            log.error("RuntimeException updating appointment status {}: {}", command.id(), ex.getMessage());
            throw new BadRequestException("appointment.update.failed");
        }
    }
}

