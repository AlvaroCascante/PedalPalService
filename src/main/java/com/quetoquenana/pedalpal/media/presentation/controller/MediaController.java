package com.quetoquenana.pedalpal.media.presentation.controller;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmUploadUseCase;
import com.quetoquenana.pedalpal.media.application.useCase.UploadMediaUseCase;
import com.quetoquenana.pedalpal.media.mapper.MediaApiMapper;
import com.quetoquenana.pedalpal.media.presentation.dto.request.ConfirmUploadRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.response.UploadMediaResponse;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.domain.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/media")
@Slf4j
public class MediaController {

    private final UploadMediaUseCase useCase;
    private final ConfirmUploadUseCase confirmUploadUseCase;
    private final MediaApiMapper mapper;
    private final CurrentUserProvider currentUserProvider;


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> upload(
            @RequestBody UploadMediaRequest request
    ) {
        log.info("POST /v1/media/upload Received request to upload: {}", request);

        SecurityUser authenticatedUser = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        UploadMediaCommand command = mapper.toCommand(
                request,
                authenticatedUser.userId(),
                authenticatedUser.isAdmin()
        );
        Set<UploadMediaResult> result = useCase.execute(command);
        Set<UploadMediaResponse> response = result.stream().map(mapper::toResponse).collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping("/confirm-upload")
    public ResponseEntity<ApiResponse> confirmUpload(
            @RequestBody ConfirmUploadRequest request
    ) {
        log.info("POST /v1/media/confirm-upload Received request to confirm upload: {}", request);
        SecurityUser authenticatedUser = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        ConfirmUploadCommand command = mapper.toCommand(request, authenticatedUser.userId());
        ConfirmedUploadResult result = confirmUploadUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse(result));
    }
}