package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.CreateBikeUploadMediaCommand;
import com.quetoquenana.pedalpal.bike.application.result.BikeUploadMediaResult;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.port.UploadMediaPort;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Use case for generating upload URLs for bike media.
 */
@RequiredArgsConstructor
@Slf4j
public class CreateBikeUploadMediaUseCase {

    private final BikeMapper mapper;
    private final BikeRepository repository;
    private final UploadMediaPort uploadMediaPort;

    /**
     * Generates signed upload URLs for the requested bike media files.
     */
    @Transactional
    public BikeUploadMediaResult execute(CreateBikeUploadMediaCommand command) {
        validate(command);

        Bike bike = repository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        try {
            UploadMediaCommand mediaRequest = mapper.toMediaUploadRequest(bike, command);
            Set<UploadMediaResult> mediaResult = uploadMediaPort.generateUploadUrls(mediaRequest);
            return mapper.toResult(mediaResult);
        } catch (BadRequestException ex) {
            log.error("BadRequestException on CreateBikeUploadMediaUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on CreateBikeUploadMediaUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.upload.media.failed");
        }
    }

    private void validate(CreateBikeUploadMediaCommand command) {
        if (command.mediaFiles() == null || command.mediaFiles().isEmpty()) {
            throw new BadRequestException("bike.upload.media.files.null");
        }
    }
}

