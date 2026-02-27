package com.quetoquenana.pedalpal.announcement.presentation.controller;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementStatusCommand;
import com.quetoquenana.pedalpal.announcement.application.query.AnnouncementQueryService;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementStatusUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementApiMapper;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.CreateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementStatusRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.response.AnnouncementResponse;
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
@RequestMapping("/v1/api/announcements")
@RequiredArgsConstructor
@Slf4j
public class AnnouncementController {

    private final CreateAnnouncementUseCase createUseCase;
    private final UpdateAnnouncementUseCase updateUseCase;
    private final UpdateAnnouncementStatusUseCase updateStatusUseCase;
    private final AnnouncementQueryService queryService;
    private final AnnouncementApiMapper apiMapper;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    @PreAuthorize("(hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreateAnnouncementRequest request
    ) {
        CreateAnnouncementCommand command = apiMapper.toCommand(request, getAuthenticatedUserId());
        AnnouncementResult result = createUseCase.execute(command);
        AnnouncementResponse response = apiMapper.toResponse(result);
        return ResponseEntity.created(URI.create("/api/announcements/" + response.id()))
                .body(new ApiResponse(response));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("(hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAnnouncementRequest request
    ) {
        UpdateAnnouncementCommand command = apiMapper.toCommand(id, request, getAuthenticatedUserId());
        AnnouncementResult result = updateUseCase.execute(command);
        AnnouncementResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("(hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAnnouncementStatusRequest request
    ) {
        UpdateAnnouncementStatusCommand command = apiMapper.toCommand(id, request);
        AnnouncementResult result = updateStatusUseCase.execute(command);
        AnnouncementResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable UUID id
    ) {
        log.info("GET /v1/api/announcements/{} Received request to get announcement by id", id);
        AnnouncementResult result = queryService.getById(id);
        AnnouncementResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/active")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getActive() {
        log.info("GET /v1/api/announcements/active Received request to get active announcements");
        List<AnnouncementResult> result = queryService.getActive();
        List<AnnouncementResponse> response = result.stream().map(apiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    private UUID getAuthenticatedUserId() {
        SecurityUser user = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        return user.userId();
    }
}
