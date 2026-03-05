package com.quetoquenana.pedalpal.media.presentation.controller;

import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.port.CurrentUserPort;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmMediaUploadUseCase;
import com.quetoquenana.pedalpal.media.application.useCase.MediaUploadUseCase;
import com.quetoquenana.pedalpal.media.presentation.dto.request.ConfirmUploadRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.response.UploadMediaResponse;
import com.quetoquenana.pedalpal.media.presentation.mapper.MediaApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for media upload and confirmation operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/media")
@Slf4j
public class MediaController {

    private final MediaUploadUseCase mediaUploadUseCase;
    private final ConfirmMediaUploadUseCase confirmMediaUploadUseCase;
    private final MediaApiMapper mapper;
    private final CurrentUserPort currentUserProvider;

    //@PostMapping("/upload") -- Not required for now, probably all upload will be handled
    // by the owner controller and the media will be stored in a separate service, but we can keep it for future use if needed
    public ResponseEntity<ApiResponse> upload(
            @RequestBody UploadMediaRequest request
    ) {
        int specsCount = request.mediaSpecs() == null ? 0 : request.mediaSpecs().size();
        log.info(
                "POST /v1/media/upload Received request to upload for referenceId={}, referenceType={}, mediaSpecsCount={}",
                request.referenceId(),
                request.referenceType(),
                specsCount
        );

        AuthenticatedUser authenticatedUser = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        UploadMediaCommand command = mapper.toCommand(
                request,
                authenticatedUser.userId(),
                authenticatedUser.isAdmin()
        );
        Set<UploadMediaResult> result = mediaUploadUseCase.execute(command);
        Set<UploadMediaResponse> response = result.stream().map(mapper::toResponse).collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping("/confirm-upload")
    public ResponseEntity<ApiResponse> confirmUpload(
            @RequestBody ConfirmUploadRequest request
    ) {
        log.info(
                "POST /v1/media/confirm-upload Received request to confirm upload for storageKey={}",
                request.storageKey()
        );
        AuthenticatedUser authenticatedUser = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        ConfirmUploadCommand command = mapper.toCommand(request, authenticatedUser.userId());
        ConfirmedUploadResult result = confirmMediaUploadUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse(result));
    }
}