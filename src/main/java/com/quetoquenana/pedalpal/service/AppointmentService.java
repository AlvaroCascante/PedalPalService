package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateAppointmentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateAppointmentRequest;
import com.quetoquenana.pedalpal.model.data.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    Appointment findById(UUID id);

    Appointment createAppointment(CreateAppointmentRequest request);

    Appointment updateAppointment(UUID id, UpdateAppointmentRequest request);

    void deleteAppointment(UUID id);

    Page<Appointment> findAll(Pageable pageable);
}

