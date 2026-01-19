package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateAppointmentTaskRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateAppointmentTaskRequest;
import com.quetoquenana.pedalpal.model.data.AppointmentTask;

import java.util.UUID;

public interface AppointmentTaskService {
    AppointmentTask addTask(UUID appointmentId, CreateAppointmentTaskRequest request);
    AppointmentTask updateTask(UUID taskId, UpdateAppointmentTaskRequest request);
    void removeTask(UUID taskId);
}

