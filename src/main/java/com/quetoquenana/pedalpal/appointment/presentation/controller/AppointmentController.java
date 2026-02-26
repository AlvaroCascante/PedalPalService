package com.quetoquenana.pedalpal.appointment.presentation.controller;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.query.AppointmentQueryService;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.CreateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentStatusUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.presentation.dto.mapper.AppointmentApiMapper;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.CreateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.UpdateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.UpdateAppointmentStatusRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentListItemResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentResponse;
import com.quetoquenana.pedalpal.common.dto.ApiResponse;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.application.SecurityUser;
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
    private final UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;
    private final AppointmentQueryService appointmentQueryService;
    private final AppointmentApiMapper apiMapper;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreateAppointmentRequest request
    ) {
        CreateAppointmentCommand command = apiMapper.toCreateCommand(request, getAuthenticatedUserId());
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

        UpdateAppointmentCommand command = apiMapper.toUpdateCommand(id, request, getAuthenticatedUserId());
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
        UUID userId = getAuthenticatedUserId();
        UpdateAppointmentStatusCommand command = apiMapper.toStatusCommand(id, request, userId);
        AppointmentResult result = updateAppointmentStatusUseCase.execute(command);
        AppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable UUID id
    ) {
        AppointmentResult result = appointmentQueryService.getById(id);
        AppointmentResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/bike/{bikeId}/upcoming")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getUpcoming(
            @PathVariable UUID bikeId
    ) {
        log.info("GET /v1/api/appointments/bike/{}/upcoming Received request to get upcoming appointments", bikeId);
        List<AppointmentListItemResult> results = appointmentQueryService.getUpcomingAppointments(bikeId);
        List<AppointmentListItemResponse> response = results.stream().map(apiMapper::toListItemResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/bike/{bikeId}/past")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getPast(
            @PathVariable UUID bikeId
    ) {
        List<AppointmentListItemResult> results = appointmentQueryService.getPastAppointments(bikeId);
        return ResponseEntity.ok(new ApiResponse(results.stream().map(apiMapper::toListItemResponse).toList()));
    }

    private UUID getAuthenticatedUserId() {
        SecurityUser user = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        return  user.userId();
    }
}
