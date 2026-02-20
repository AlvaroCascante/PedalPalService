package com.quetoquenana.pedalpal.bike.presentation.controller;

import com.quetoquenana.pedalpal.bike.application.command.*;
import com.quetoquenana.pedalpal.bike.application.useCase.*;
import com.quetoquenana.pedalpal.bike.presentation.dto.request.*;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.bike.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.bike.domain.enums.BikeComponentStatus;
import com.quetoquenana.pedalpal.security.application.SecurityUser;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeResponse;
import com.quetoquenana.pedalpal.bike.presentation.mapper.BikeApiMapper;
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

    private final BikeQueryService bikeQueryService;

    private final BikeApiMapper bikeApiMapper;

    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable("id") UUID id,
            @RequestParam(name = "status", required = false) Set<BikeComponentStatus> statuses
    ) {
        log.info("GET /v1/api/bikes/{} Received request to get bike by id", id);
        BikeResult result = bikeQueryService.getById(id, getAuthenticatedUserId());
        BikeResponse response = bikeApiMapper.toResponse(result, statuses);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/active")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> findActive() {
        log.info("GET /v1/api/bikes/active Received request to fetch active bikes");
        List<BikeResult> result = bikeQueryService.fetchActiveByOwnerId(getAuthenticatedUserId());
        List<BikeResponse> response = result.stream().map(bikeApiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreateBikeRequest request
    ) {
        log.info("POST /v1/api/bikes Received request to create bike: {}", request);
        // Map the incoming request to a command object
        CreateBikeCommand command = bikeApiMapper.toCommand(getAuthenticatedUserId(), request);

        // Execute the use case to create the bike
        BikeResult result = createBikeUseCase.execute(command);

        // Map the result to a response DTO
        BikeResponse response = bikeApiMapper.toResponse(result);

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
        UpdateBikeCommand command = bikeApiMapper.toCommand(id, getAuthenticatedUserId(), request);
        BikeResult result = updateBikeUseCase.execute(command);
        BikeResponse response = bikeApiMapper.toResponse(result);

        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateBikeStatusRequest request
    ) {
        log.info("PATCH /v1/api/bikes/{}/status Received request to update bike status: {}", id, request);
        UpdateBikeStatusCommand command = bikeApiMapper.toCommand(id, getAuthenticatedUserId(), request);
        BikeResult result = updateBikeStatusUseCase.execute(command);
        BikeResponse response = bikeApiMapper.toResponse(result);

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
        AddBikeComponentCommand command = bikeApiMapper.toCommand(id, null, getAuthenticatedUserId(), request);

        // Execute the use case to create the bike
        BikeResult result = addBikeComponentUseCase.execute(command);

        // Map the result to a response DTO
        BikeResponse response = bikeApiMapper.toResponse(result);

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
        UpdateBikeComponentCommand command = bikeApiMapper.toCommand(bikeId, componentId, getAuthenticatedUserId(), request);
        BikeResult result = updateBikeComponentUseCase.execute(command);
        BikeResponse response = bikeApiMapper.toResponse(result);

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
        UpdateBikeComponentStatusCommand command = bikeApiMapper.toCommand(bikeId, componentId, getAuthenticatedUserId(), request);
        BikeResult result = updateBikeComponentStatusUseCase.execute(command);
        BikeResponse response = bikeApiMapper.toResponse(result);

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
        AddBikeComponentCommand command = bikeApiMapper.toCommand(bikeId, componentId, getAuthenticatedUserId(), request);

        // Execute the use case to create the bike
        BikeResult result = replaceBikeComponentUseCase.execute(command);

        // Map the result to a response DTO
        BikeResponse response = bikeApiMapper.toResponse(result);

        return ResponseEntity.created(URI.create("/api/bikes/" + response.id()))
                .body(new ApiResponse(response));
    }

    private UUID getAuthenticatedUserId() {
        SecurityUser user = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        return  user.userId();
    }
}
