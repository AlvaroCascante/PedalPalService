package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.AppointmentTask;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepositoryCustom {
    AppointmentTask createTask(UUID appointmentId, AppointmentTask task);
    AppointmentTask updateTask(AppointmentTask task);
    void deleteTask(UUID taskId);
    List<AppointmentTask> findTasksByAppointmentId(UUID appointmentId);
}

