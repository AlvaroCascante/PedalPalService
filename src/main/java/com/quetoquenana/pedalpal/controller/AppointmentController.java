package com.quetoquenana.pedalpal.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateAppointmentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateAppointmentRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.data.Appointment;
import com.quetoquenana.pedalpal.service.AppointmentService;
import com.quetoquenana.pedalpal.util.JsonViewPageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE;
import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE_SIZE;

@RestController
@RequestMapping("/v1/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping()
    @JsonView(Appointment.AppointmentList.class)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchAllAppointments(
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /v1/api/appointments/all called");
        Page<Appointment> entities = appointmentService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/{id}")
    @JsonView(Appointment.AppointmentDetail.class)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MECHANIC')")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        log.info("GET /v1/api/appointments/{} called", id);
        Appointment entity = appointmentService.findById(id);
        return ResponseEntity.ok(new ApiResponse(entity));
    }

    @PostMapping
    @JsonView(Appointment.AppointmentDetail.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateAppointmentRequest request) {
        log.info("POST /v1/api/appointments Received request to create appointment: {}", request);
        Appointment saved = appointmentService.createAppointment(request);
        return ResponseEntity.created(URI.create("/v1/api/appointments/" + saved.getId()))
                .body(new ApiResponse(saved));
    }

    @PutMapping("/{id}")
    @JsonView(Appointment.AppointmentDetail.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateAppointment(@PathVariable UUID id, @Valid @RequestBody UpdateAppointmentRequest request) {
        log.info("PUT /v1/api/appointments/{} Received request to update appointment: {}", id, request);
        Appointment saved = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        log.info("DELETE /v1/api/appointments/{} called", id);
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }


}

