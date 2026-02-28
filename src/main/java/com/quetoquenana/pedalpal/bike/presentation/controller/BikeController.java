package com.quetoquenana.pedalpal.bike.presentation.controller;

import com.quetoquenana.pedalpal.bike.application.command.*;
import com.quetoquenana.pedalpal.bike.application.query.BikeHistoryQueryService;
import com.quetoquenana.pedalpal.bike.application.result.BikeHistoryResult;
import com.quetoquenana.pedalpal.bike.application.useCase.*;
import com.quetoquenana.pedalpal.bike.presentation.dto.request.*;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeHistoryResponse;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.bike.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.security.domain.model.SecurityUser;
import com.quetoquenana.pedalpal.common.presentation.dto.ApiResponse;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeResponse;
import com.quetoquenana.pedalpal.bike.mapper.BikeApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/bikes")
@RequiredArgsConstructor
@Slf4j
public class BikeController {

    private final AddBikeComponentUseCase addBikeComponentUseCase;
    private final CreateBikeUseCase createBikeUseCase;
    private final ReplaceBikeComponentUseCase replaceBikeComponentUseCase;
    private final UpdateBikeComponentUseCase updateBikeComponentUseCase;
    private final UpdateBikeComponentStatusUseCase updateBikeComponentStatusUseCase;
    private final UpdateBikeStatusUseCase updateBikeStatusUseCase;
    private final UpdateBikeUseCase updateBikeUseCase;

    private final BikeHistoryQueryService bikeHistoryQueryService;
    private final BikeQueryService queryService;

    private final BikeApiMapper apiMapper;

    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable("id") UUID id,
            @RequestParam(name = "componentStatus", required = false) Set<BikeComponentStatus> componentStatuses
    ) {
        log.info("GET /v1/api/bikes/{} Received request to get bike by id", id);
        BikeResult result = queryService.getById(id, getAuthenticatedUserId());
        BikeResponse response = apiMapper.toResponse(result, componentStatuses);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getBikeHistory(@PathVariable("id") UUID id) {
        log.info("GET /v1/api/bikes/{}/history Received request to get bike history", id);
        List<BikeHistoryResult> result = bikeHistoryQueryService.findByBikeId(id, getAuthenticatedUserId());
        List<BikeHistoryResponse> response = result.stream().map(apiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/active")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> findActive() {
        log.info("GET /v1/api/bikes/active Received request to find active bikes");
        List<BikeResult> result = queryService.findActiveByOwnerId(getAuthenticatedUserId());
        List<BikeResponse> response = result.stream().map(apiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreateBikeRequest request
    ) {
        log.info("POST /v1/api/bikes Received request to create bike: {}", request);
        // Map the incoming request to a command object
        CreateBikeCommand command = apiMapper.toCommand(getAuthenticatedUserId(), request);

        // Execute the use case to create the bike
        BikeResult result = createBikeUseCase.execute(command);

        // Map the result to a response DTO
        BikeResponse response = apiMapper.toResponse(result);

        return ResponseEntity.created(URI.create("/api/bikes/" + response.id()))
                .body(new ApiResponse(response));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateBikeRequest request
    ) {
        log.info("PATCH /v1/api/bikes/{} Received request to update bike: {}", id, request);
        UpdateBikeCommand command = apiMapper.toCommand(id, getAuthenticatedUserId(), request);
        BikeResult result = updateBikeUseCase.execute(command);
        BikeResponse response = apiMapper.toResponse(result);

        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateBikeStatusRequest request
    ) {
        log.info("PATCH /v1/api/bikes/{}/status Received request to update bike status: {}", id, request);
        UpdateBikeStatusCommand command = apiMapper.toCommand(id, getAuthenticatedUserId(), request);
        BikeResult result = updateBikeStatusUseCase.execute(command);
        BikeResponse response = apiMapper.toResponse(result);

        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping("/{id}/components")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> addBikeComponent(
            @PathVariable("id") UUID id,
            @Valid @RequestBody AddBikeComponentRequest request
    ) {
        log.info("POST /v1/api/bikes/{}/components Received request to add a bike component: {}", id, request);
        // Map the incoming request to a command object
        AddBikeComponentCommand command = apiMapper.toCommand(id, null, getAuthenticatedUserId(), request);

        // Execute the use case to create the bike
        BikeResult result = addBikeComponentUseCase.execute(command);

        // Map the result to a response DTO
        BikeResponse response = apiMapper.toResponse(result);

        return ResponseEntity.created(URI.create("/api/bikes/" + response.id()))
                .body(new ApiResponse(response));
    }


    @PatchMapping("/{bikeId}/components/{componentId}")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> updateBikeComponent(
            @PathVariable("bikeId") UUID bikeId,
            @PathVariable("componentId") UUID componentId,
            @Valid @RequestBody UpdateBikeComponentRequest request
    ) {
        log.info("PATCH /v1/api/bikes/{}/components/{} Received request to update bike component: {}", bikeId, componentId, request);
        UpdateBikeComponentCommand command = apiMapper.toCommand(bikeId, componentId, getAuthenticatedUserId(), request);
        BikeResult result = updateBikeComponentUseCase.execute(command);
        BikeResponse response = apiMapper.toResponse(result);

        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/{bikeId}/components/{componentId}/status")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> updateBikeComponentStatus(
            @PathVariable("bikeId") UUID bikeId,
            @PathVariable("componentId") UUID componentId,
            @Valid @RequestBody UpdateBikeComponentStatusRequest request
    ) {
        log.info("PATCH /v1/api/bikes/{}/components/{}/status Received request to update bike component status: {}", bikeId, componentId, request);
        UpdateBikeComponentStatusCommand command = apiMapper.toCommand(bikeId, componentId, getAuthenticatedUserId(), request);
        BikeResult result = updateBikeComponentStatusUseCase.execute(command);
        BikeResponse response = apiMapper.toResponse(result);

        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping("/{bikeId}/components/{componentId}/replace")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> replaceBikeComponent(
            @PathVariable("bikeId") UUID bikeId,
            @PathVariable("componentId") UUID componentId,
            @Valid @RequestBody AddBikeComponentRequest request
    ) {
        log.info("POST /v1/api/bikes/{}/components/{}/replace Received request to replace a bike component: {}", bikeId, componentId, request);
        // Map the incoming request to a command object
        AddBikeComponentCommand command = apiMapper.toCommand(bikeId, componentId, getAuthenticatedUserId(), request);

        // Execute the use case to create the bike
        BikeResult result = replaceBikeComponentUseCase.execute(command);

        // Map the result to a response DTO
        BikeResponse response = apiMapper.toResponse(result);

        return ResponseEntity.created(URI.create("/api/bikes/" + response.id()))
                .body(new ApiResponse(response));
    }

    private UUID getAuthenticatedUserId() {
        SecurityUser user = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        return  user.userId();
    }
}
