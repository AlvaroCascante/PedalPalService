package com.quetoquenana.pedalpal.media.presentation.controller;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.query.MediaQueryService;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmMediaUploadUseCase;
import com.quetoquenana.pedalpal.media.presentation.dto.response.MediaResponse;
import com.quetoquenana.pedalpal.media.presentation.mapper.MediaApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    private final MediaQueryService queryService;
    private final MediaApiMapper apiMapper;

    @GetMapping("/{referenceType}/{referenceId}")
    public ResponseEntity<ApiResponse> getMediaByReference(
            @PathVariable("referenceType") String referenceType,
            @PathVariable("referenceId") UUID referenceId
    ) {
        log.info("GET /v1/api/media/{}/{} Received request to get media by reference", referenceType, referenceId);
        List<MediaResult> result = queryService.getByReferenceIdAndReferenceType(referenceId, referenceType);
        List<MediaResponse> response = result.stream().map(apiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse> confirmUpload(
            @PathVariable("id") UUID id
    ) {
        log.info("POST /v1/api/media/{}/confirm Received request to confirm upload", id);

        ConfirmUploadCommand command = apiMapper.toCommand(id);
        confirmMediaUploadUseCase.execute(command);

        MediaResult result = queryService.getById(id);
        MediaResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }
}