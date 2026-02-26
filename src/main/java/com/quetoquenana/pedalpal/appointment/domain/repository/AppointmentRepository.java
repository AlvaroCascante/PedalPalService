package com.quetoquenana.pedalpal.appointment.domain.repository;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);

    Optional<Appointment> getById(UUID id);

    List<Appointment> findUpcomingByBikeId(UUID bikeId, Instant now);

    List<Appointment> findPastByBikeId(UUID bikeId, Instant now);
}

