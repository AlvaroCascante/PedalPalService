package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.result.ConfirmAppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceOrder.application.port.ServiceOrderPort;
import com.quetoquenana.pedalpal.serviceOrder.application.result.ServiceOrderResult;
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
public class CancelAppointmentUseCase {

    private final AppointmentMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final ServiceOrderPort serviceOrderPort;

    @Transactional
    public ConfirmAppointmentResult execute(UpdateAppointmentStatusCommand command) {
        Appointment appointment = appointmentRepository.getById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found"));

        try {
            validateStatusTransition(appointment.getStatus());
            applyPatch(appointment, command);
            appointmentRepository.save(appointment);
            ServiceOrderResult serviceOrderResult = serviceOrderPort.cancelServiceOrder(appointment.getId(), command.reason());

            return new ConfirmAppointmentResult(
                    mapper.toResult(appointment),
                    serviceOrderResult
            );
        } catch (RuntimeException ex) {
            log.error("RuntimeException updating appointment status {}: {}", command.id(), ex.getMessage());
            throw new BadRequestException("appointment.update.failed");
        }
    }

    private void validateStatusTransition(AppointmentStatus status) {
        switch (status) {
            case REJECTED:
            case CANCELLED:
            case NO_SHOW:
            case COMPLETED:
                throw new BadRequestException("appointment.update.status.closed");
            default:
                break;
        }
    }

    private void applyPatch(Appointment appointment, UpdateAppointmentStatusCommand command) {
        if (command.status() != null) {
            String value = command.status();
            if (value.trim().isEmpty()) {
                throw new BadRequestException("appointment.status.required");
            }
            AppointmentStatus status = AppointmentStatus.from(value);
            switch (status) {
                case REJECTED:
                case CANCELLED:
                    appointment.confirm();
                    appointment.cancel(status, command.reason());
                    break;
                default:
                    throw new BadRequestException("appointment.status.invalid", status) ;
            }
        }
    }
}

