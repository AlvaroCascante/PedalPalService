package com.quetoquenana.pedalpal.presentation.controller;

import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.command.CreateBikeResult;
import com.quetoquenana.pedalpal.application.useCase.CreateBikeUseCase;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.util.SecurityUtils;
import com.quetoquenana.pedalpal.presentation.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.presentation.dto.api.response.CreateBikeResponse;
import com.quetoquenana.pedalpal.presentation.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.presentation.mapper.BikeApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Locale;

@RestController
@RequestMapping("/v1/api/bikes")
@RequiredArgsConstructor
@Slf4j
public class BikeController {

    private final CreateBikeUseCase  createBikeUseCase;
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
}
