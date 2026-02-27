package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * Use case for updating the status of an appointment.
 * It retrieves the appointment, updates its status based on the command, and saves it.
 * It also handles exceptions and logs errors.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAppointmentStatusUseCase {

    private final AppointmentMapper mapper;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public AppointmentResult execute(UpdateAppointmentStatusCommand command) {
        Appointment appointment = appointmentRepository.getById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found"));

        try {
            applyPatch(appointment, command);
            Appointment saved = appointmentRepository.save(appointment);
            return mapper.toResult(saved);
        } catch (RuntimeException ex) {
            log.error("RuntimeException updating appointment status {}: {}", command.id(), ex.getMessage());
            throw new BadRequestException("appointment.update.failed");
        }
    }

    private void applyPatch(Appointment appointment, UpdateAppointmentStatusCommand command) {
        if (command.status() != null) {
            rejectBlank(command.status());
            appointment.setStatus(AppointmentStatus.from(command.status()));
        }
    }

    private void rejectBlank(String value) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException("appointment.update.status.required");
        }
    }
}

