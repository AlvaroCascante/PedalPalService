package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateAppointmentTaskRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateAppointmentTaskRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.data.Appointment;
import com.quetoquenana.pedalpal.model.data.AppointmentTask;
import com.quetoquenana.pedalpal.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.repository.AppointmentTaskRepository;
import com.quetoquenana.pedalpal.service.AppointmentTaskService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppointmentTaskServiceImpl implements AppointmentTaskService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentTaskRepository taskRepository;

    public AppointmentTaskServiceImpl(AppointmentRepository appointmentRepository, AppointmentTaskRepository taskRepository) {
        this.appointmentRepository = appointmentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public AppointmentTask addTask(UUID appointmentId, CreateAppointmentTaskRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RecordNotFoundException("appointment.not.found", appointmentId));
        AppointmentTask task = AppointmentTask.createFromRequest(request, appointment);

        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setCreatedBy(user.username());
        task.setUpdatedAt(now);
        task.setUpdatedBy(user.username());
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public AppointmentTask updateTask(UUID taskId, UpdateAppointmentTaskRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        AppointmentTask task = taskRepository.findById(taskId).orElseThrow(() -> new RecordNotFoundException("appointment.task.not.found", taskId));
        task.updateFromRequest(request);
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdatedBy(user.username());
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void removeTask(UUID taskId) {
        AppointmentTask task = taskRepository.findById(taskId).orElseThrow(() -> new RecordNotFoundException("appointment.task.not.found", taskId));
        taskRepository.delete(task);
    }
}
