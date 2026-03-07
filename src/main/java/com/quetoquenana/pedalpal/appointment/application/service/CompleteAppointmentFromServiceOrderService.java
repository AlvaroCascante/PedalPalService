package com.quetoquenana.pedalpal.appointment.application.service;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Application service that completes an appointment when its service order is completed.
 */
@RequiredArgsConstructor
public class CompleteAppointmentFromServiceOrderService {

    private final AppointmentRepository appointmentRepository;

    /**
     * Marks the appointment as completed.
     */
    public void complete(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found", appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return;
        }

        appointment.changeStatusTo(
                AppointmentStatus.COMPLETED,
                null,
                null
        );

        appointmentRepository.save(appointment);
    }
}
