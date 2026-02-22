package com.quetoquenana.pedalpal.common.domain.repository;

import com.quetoquenana.pedalpal.common.domain.model.AppointmentTask;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentTaskRepository {

    Optional<AppointmentTask> getById(UUID appointmentTaskId);

    AppointmentTask save(AppointmentTask appointmentTask);

    AppointmentTask update(UUID appointmentTaskId, AppointmentTask appointmentTask);

    void deleteById(UUID appointmentTaskId);
}

