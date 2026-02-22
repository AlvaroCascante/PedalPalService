package com.quetoquenana.pedalpal.common.domain.repository;

import com.quetoquenana.pedalpal.common.domain.model.Appointment;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {

    Optional<Appointment> getById(UUID appointmentId);

    Appointment save(Appointment appointment);

    Appointment update(UUID appointmentId, Appointment appointment);

    void deleteById(UUID appointmentId);
}
