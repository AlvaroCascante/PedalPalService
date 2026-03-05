package com.quetoquenana.pedalpal.announcement.presentation.controller;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.query.AnnouncementQueryService;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.application.usecase.ActivateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.InactivateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.presentation.mapper.AnnouncementApiMapper;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.CreateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.response.AnnouncementResponse;
import com.quetoquenana.pedalpal.common.application.port.CurrentUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/announcements")
@RequiredArgsConstructor
@Slf4j
public class AnnouncementController {

    private final ActivateAnnouncementUseCase activateAnnouncementUseCase;
    private final InactivateAnnouncementUseCase inactivateAnnouncementUseCase;
    private final CreateAnnouncementUseCase createUseCase;
    private final UpdateAnnouncementUseCase updateUseCase;
    private final AnnouncementQueryService queryService;
    private final AnnouncementApiMapper apiMapper;
    private final CurrentUserPort currentUserProvider;

    @PostMapping
    @PreAuthorize("(hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreateAnnouncementRequest request
    ) {
        AuthenticatedUser authenticatedUser = getAuthenticatedUserId();
        CreateAnnouncementCommand command = apiMapper.toCommand(authenticatedUser.userId(), request);
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
        AuthenticatedUser authenticatedUser = getAuthenticatedUserId();
        UpdateAnnouncementCommand command = apiMapper.toCommand(id, authenticatedUser.userId(), request);
        AnnouncementResult result = updateUseCase.execute(command);
        AnnouncementResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("(hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> activate(
            @PathVariable UUID id
    ) {
        AuthenticatedUser authenticatedUser = getAuthenticatedUserId();
        UpdateAnnouncementCommand command = apiMapper.toCommand(
                id,
                authenticatedUser.userId()
        );
        AnnouncementResult result = activateAnnouncementUseCase.execute(command);
        AnnouncementResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }


    @PatchMapping("/{id}/inactivate")
    @PreAuthorize("(hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> inactivate(
            @PathVariable UUID id
    ) {
        AuthenticatedUser authenticatedUser = getAuthenticatedUserId();
        UpdateAnnouncementCommand command = apiMapper.toCommand(
                id,
                authenticatedUser.userId()
        );
        AnnouncementResult result = inactivateAnnouncementUseCase.execute(command);
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
        Set<AnnouncementResponse> response = result.stream().map(apiMapper::toResponse).collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse(response));
    }

    private AuthenticatedUser getAuthenticatedUserId() {
        return currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
    }
}
