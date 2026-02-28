package com.quetoquenana.pedalpal.media.presentation.controller;
import com.quetoquenana.pedalpal.common.presentation.dto.ApiResponse;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.command.GenerateUploadUrlCommand;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.application.result.GenerateUploadUrlResult;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmUploadUseCase;
import com.quetoquenana.pedalpal.media.application.useCase.GenerateUploadUrlUseCase;
import com.quetoquenana.pedalpal.media.mapper.MediaApiMapper;
import com.quetoquenana.pedalpal.media.presentation.dto.request.ConfirmUploadRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.response.GenerateUploadUrlResponse;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.domain.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/media")
@Slf4j
public class MediaController {

    private final GenerateUploadUrlUseCase useCase;
    private final ConfirmUploadUseCase confirmUploadUseCase;
    private final MediaApiMapper mapper;
    private final CurrentUserProvider currentUserProvider;

    @Value("${app.media.default-provider}")
    private String defaultProvider;

    @PostMapping("/upload-url")
    public ResponseEntity<ApiResponse> generateUploadUrl(
            @RequestBody UploadMediaRequest request
    ) {
        log.info("POST /v1/media/upload-url Received request to generate upload URL: {}", request);

        SecurityUser authenticatedUser = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        GenerateUploadUrlCommand command = mapper.toCommand(
                request,
                authenticatedUser.userId(),
                defaultProvider,
                authenticatedUser.isAdmin()
        );
        GenerateUploadUrlResult result = useCase.execute(command);
        GenerateUploadUrlResponse response = mapper.toResponse(result);
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