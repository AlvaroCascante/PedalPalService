package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAppointmentUseCase {

    private final AppointmentMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final ProductRepository productRepository;

    @Transactional
    public AppointmentResult execute(UpdateAppointmentCommand command) {
        Appointment appointment = appointmentRepository.getById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found"));

        try {
            applyPatch(appointment, command);
            appointmentRepository.save(appointment);
            return mapper.toResult(appointment);
        } catch (BadRequestException ex) {
            log.error("BadRequestException on UpdateAppointmentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateAppointmentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.update.failed");
        }
    }

    private void applyPatch(Appointment appointment, UpdateAppointmentCommand command) {
        if (command.scheduledAt() != null) {
            rejectBlank(command.scheduledAt().toString(), "bike.update.name.blank");
            appointment.setScheduledAt(command.scheduledAt());
        }

        if (command.notes() != null) {
            rejectBlank(command.notes(), "bike.update.type.blank");
            appointment.setNotes(command.notes());
        }
    }

    private void rejectBlank(String value, String messageKey) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException(messageKey);
        }
    }

}

