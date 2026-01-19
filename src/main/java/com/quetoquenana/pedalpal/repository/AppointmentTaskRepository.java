package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.local.AppointmentTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentTaskRepository extends JpaRepository<AppointmentTask, UUID> {

}

