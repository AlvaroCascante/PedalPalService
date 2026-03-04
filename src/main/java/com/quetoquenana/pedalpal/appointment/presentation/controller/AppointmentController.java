package com.quetoquenana.pedalpal.appointment.presentation.controller;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.query.AppointmentQueryService;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.ConfirmAppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.*;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentApiMapper;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.CreateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.UpdateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.UpdateAppointmentStatusRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentListItemResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.ConfirmAppointmentResponse;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.common.application.port.CurrentUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final CancelAppointmentUseCase cancelAppointmentUseCase;
    private final ConfirmAppointmentUseCase confirmAppointmentUseCase;
    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;
    private final UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;
    private final AppointmentQueryService queryService;
    private final AppointmentApiMapper apiMapper;
    private final CurrentUserPort currentUserProvider;

    @PostMapping
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreateAppointmentRequest request
    ) {
        CreateAppointmentCommand command = apiMapper.toCommand(request, getAuthenticatedUserId());
        AppointmentResult result = createAppointmentUseCase.execute(command);
        AppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.created(URI.create("/api/appointments/" + response.id()))
                .body(new ApiResponse(response));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentRequest request
    ) {

        UpdateAppointmentCommand command = apiMapper.toCommand(id, request);
        AppointmentResult result = updateAppointmentUseCase.execute(command);
        AppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request
    ) {
        UpdateAppointmentStatusCommand command = apiMapper.toCommand(id, request);
        AppointmentResult result = updateAppointmentStatusUseCase.execute(command);
        AppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/{id}/cance")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> cancelAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request
    ) {
        UpdateAppointmentStatusCommand command = apiMapper.toCommand(id, request);
        ConfirmAppointmentResult result = cancelAppointmentUseCase.execute(command);
        ConfirmAppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> confirmAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request
    ) {
        UpdateAppointmentStatusCommand command = apiMapper.toCommand(id, request);
        ConfirmAppointmentResult result = confirmAppointmentUseCase.execute(command);
        ConfirmAppointmentResponse response = apiMapper.toResponse(result);
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
        Set<AppointmentListItemResult> results = queryService.getUpcomingAppointments(bikeId);
        Set<AppointmentListItemResponse> response = results.stream().map(apiMapper::toListItemResponse).collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/bike/{bikeId}/past")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getPast(
            @PathVariable UUID bikeId
    ) {
        Set<AppointmentListItemResult> results = queryService.getPastAppointments(bikeId);
        return ResponseEntity.ok(new ApiResponse(results.stream().map(apiMapper::toListItemResponse).collect(Collectors.toSet())));
    }

    private UUID getAuthenticatedUserId() {
        AuthenticatedUser user = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        return user.userId();
    }
}
