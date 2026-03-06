package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/*
 * Use case for updating an appointment.
 * It retrieves the appointment, applies updates based on the command, and saves it.
 * It also handles exceptions and logs errors.
 */
@Slf4j
@RequiredArgsConstructor
public class UpdateAppointmentUseCase {

    private final AppointmentMapper mapper;
    private final AppointmentRepository repository;
    private final AuthenticatedUserPort authenticatedUserPort;

    @Transactional
    public AppointmentResult execute(UpdateAppointmentCommand command) {
        AuthenticatedUser currentUser = authenticatedUserPort.getAuthenticatedUser().
                orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        Appointment appointment = repository.findById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found"));

        mapper.applyPatch(appointment, command);
        repository.save(appointment);
        return mapper.toResult(appointment);
    }
}

