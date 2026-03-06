package com.quetoquenana.pedalpal.media.presentation.controller;

import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmMediaUploadUseCase;
import com.quetoquenana.pedalpal.media.presentation.mapper.MediaApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for media upload and confirmation operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/media")
@Slf4j
public class MediaController {

    private final ConfirmMediaUploadUseCase confirmMediaUploadUseCase;
    private final MediaApiMapper mapper;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse> confirmUpload(
            @PathVariable("id") UUID id
    ) {
        log.info("POST /v1/api/media/{}/confirm Received request to confirm upload", id);
        ConfirmUploadCommand command = mapper.toCommand(id);
        ConfirmedUploadResult result = confirmMediaUploadUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse(result));
    }
}