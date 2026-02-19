package com.quetoquenana.pedalpal.presentation.controller;

import com.quetoquenana.pedalpal.application.command.*;
import com.quetoquenana.pedalpal.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.application.useCase.AddBikeComponentUseCase;
import com.quetoquenana.pedalpal.application.useCase.CreateBikeUseCase;
import com.quetoquenana.pedalpal.application.useCase.UpdateBikeStatusUseCase;
import com.quetoquenana.pedalpal.application.useCase.UpdateBikeUseCase;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.util.SecurityUtils;
import com.quetoquenana.pedalpal.presentation.dto.api.request.AddBikeComponentRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.UpdateBikeStatusRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.presentation.dto.api.response.BikeResponse;
import com.quetoquenana.pedalpal.presentation.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.presentation.mapper.BikeApiMapper;
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
@RequestMapping("/v1/api/bikes")
@RequiredArgsConstructor
@Slf4j
public class BikeController {

    private final AddBikeComponentUseCase addBikeComponentUseCase;
    private final CreateBikeUseCase  createBikeUseCase;
    private final UpdateBikeUseCase updateBikeUseCase;
    private final UpdateBikeStatusUseCase updateBikeStatusUseCase;
    private final BikeQueryService bikeQueryService;
    private final BikeApiMapper bikeApiMapper;

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable("id") UUID id
    ) {
        log.info("GET /v1/api/bikes/{} Received request to get bike by id", id);

        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        BikeResult result = bikeQueryService.getById(id, user.userId());
        BikeResponse response = bikeApiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/active")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> findActive() {
        log.info("GET /v1/api/bikes/active Received request to fetch active bikes");

        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        List<BikeResult> result = bikeQueryService.fetchActiveByOwnerId(user.userId());
        List<BikeResponse> response = result.stream().map(bikeApiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreateBikeRequest request
    ) {
        log.info("POST /v1/api/bikes Received request to create bike: {}", request);

        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        // Map the incoming request to a command object
        CreateBikeCommand command = bikeApiMapper.toCommand(request, user.userId());

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

        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        UpdateBikeCommand command = bikeApiMapper.toCommand(request, id, user.userId());
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

        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        UpdateBikeStatusCommand command = bikeApiMapper.toCommand(request, id, user.userId());
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

        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        // Map the incoming request to a command object
        AddBikeComponentCommand command = bikeApiMapper.toCommand(id, user.userId(), request);

        // Execute the use case to create the bike
        BikeResult result = addBikeComponentUseCase.execute(command);

        // Map the result to a response DTO
        BikeResponse response = bikeApiMapper.toResponse(result);

        return ResponseEntity.created(URI.create("/api/bikes/" + response.id()))
                .body(new ApiResponse(response));
    }
}
