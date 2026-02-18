package com.quetoquenana.pedalpal.presentation.controller;

import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.command.CreateBikeResult;
import com.quetoquenana.pedalpal.application.command.UpdateBikeCommand;
import com.quetoquenana.pedalpal.application.command.UpdateBikeResult;
import com.quetoquenana.pedalpal.application.useCase.CreateBikeUseCase;
import com.quetoquenana.pedalpal.application.useCase.UpdateBikeUseCase;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.util.SecurityUtils;
import com.quetoquenana.pedalpal.presentation.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.presentation.dto.api.response.CreateBikeResponse;
import com.quetoquenana.pedalpal.presentation.dto.api.response.UpdateBikeResponse;
import com.quetoquenana.pedalpal.presentation.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.presentation.mapper.BikeApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/api/bikes")
@RequiredArgsConstructor
@Slf4j
public class BikeController {

    private final CreateBikeUseCase  createBikeUseCase;
    private final UpdateBikeUseCase updateBikeUseCase;
    private final BikeApiMapper bikeApiMapper;

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
        CreateBikeResult result = createBikeUseCase.execute(command);

        // Map the result to a response DTO
        CreateBikeResponse response = bikeApiMapper.toResponse(result);

        return ResponseEntity.created(URI.create("/api/bikes/" + response.id()))
                .body(new ApiResponse(response));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") java.util.UUID id,
            @Valid @RequestBody UpdateBikeRequest request
    ) {
        log.info("PATCH /v1/api/bikes/{} Received request to update bike: {}", id, request);

        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        UpdateBikeCommand command = bikeApiMapper.toCommand(request, id, user.userId());
        UpdateBikeResult result = updateBikeUseCase.execute(command);
        UpdateBikeResponse response = bikeApiMapper.toResponse(result);

        return ResponseEntity.ok(new ApiResponse(response));
    }
}
