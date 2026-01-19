package com.quetoquenana.pedalpal.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateAppointmentTaskRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateAppointmentTaskRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.local.AppointmentTask;
import com.quetoquenana.pedalpal.service.AppointmentTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentTaskController {

    private final AppointmentTaskService taskService;

    @PostMapping("/{appointmentId}/tasks")
    @JsonView(AppointmentTask.AppointmentTaskDetail.class)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MECHANIC')")
    public ResponseEntity<ApiResponse> addTask(@PathVariable UUID appointmentId, @Valid @RequestBody CreateAppointmentTaskRequest request) {
        log.info("POST /v1/api/appointments/{}/tasks called", appointmentId);
        AppointmentTask saved = taskService.addTask(appointmentId, request);
        return ResponseEntity.created(URI.create("/v1/api/appointments/tasks/" + saved.getId())).body(new ApiResponse(saved));
    }

    @PatchMapping("/tasks/{taskId}")
    @JsonView(AppointmentTask.AppointmentTaskDetail.class)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MECHANIC')")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable UUID taskId, @Valid @RequestBody UpdateAppointmentTaskRequest request) {
        log.info("PATCH /v1/api/appointments/tasks/{} called", taskId);
        AppointmentTask saved = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @DeleteMapping("/tasks/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MECHANIC')")
    public ResponseEntity<Void> removeTask(@PathVariable UUID taskId) {
        log.info("DELETE /v1/api/appointments/tasks/{} called", taskId);
        taskService.removeTask(taskId);
        return ResponseEntity.noContent().build();
    }
}

