package com.quetoquenana.pedalpal.appointment.presentation.controller;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.query.AppointmentQueryService;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.ChangeAppointmentStatusResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.ChangeAppointmentStatusUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.CreateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.ChangeAppointmentStatusRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.CreateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.UpdateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentListItemResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.ChangeAppointmentStatusResponse;
import com.quetoquenana.pedalpal.appointment.presentation.mapper.AppointmentApiMapper;
import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;
    private final ChangeAppointmentStatusUseCase changeAppointmentStatusUseCase;
    private final AppointmentQueryService queryService;
    private final AppointmentApiMapper apiMapper;

    @PostMapping
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreateAppointmentRequest request
    ) {
        CreateAppointmentCommand command = apiMapper.toCommand(request);
        AppointmentResult result = createAppointmentUseCase.execute(command);
        AppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.created(URI.create("/api/appointments/" + response.id()))
                .body(new ApiResponse(response));
    }

    @PatchMapping("/{id}/reschedule")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> reschedule(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentRequest request
    ) {

        UpdateAppointmentCommand command = apiMapper.toCommand(id, request);
        AppointmentResult result = updateAppointmentUseCase.execute(command);
        AppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN')) or (hasRole('TECHNICIAN'))")
    public ResponseEntity<ApiResponse> changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeAppointmentStatusRequest request
    ) {
        ChangeAppointmentStatusCommand command = apiMapper.toCommand(id, request);
        ChangeAppointmentStatusResult result = changeAppointmentStatusUseCase.execute(command);
        ChangeAppointmentStatusResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable UUID id
    ) {
        AppointmentResult result = queryService.getById(id);
        AppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/bike/{bikeId}/upcoming")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getUpcoming(
            @PathVariable UUID bikeId
    ) {
        log.info("GET /v1/api/appointments/bike/{}/upcoming Received request to get upcoming appointments", bikeId);
        List<AppointmentListItemResult> results = queryService.getUpcomingAppointments(bikeId);
        List<AppointmentListItemResponse> response = results.stream().map(apiMapper::toListItemResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/bike/{bikeId}/past")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getPast(
            @PathVariable UUID bikeId
    ) {
        List<AppointmentListItemResult> results = queryService.getPastAppointments(bikeId);
        List<AppointmentListItemResponse> response = results.stream().map(apiMapper::toListItemResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }
}
