package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.data.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}

