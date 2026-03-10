package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.CreateBikeUploadMediaCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeMediaResult;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.media.application.port.UploadMediaPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use case for generating upload URLs for bike media.
 */
@Slf4j
@RequiredArgsConstructor
public class UploadBikeMediaUseCase {

    private final AuthenticatedUserPort authenticatedUserPort;
    private final BikeMapper mapper;
    private final BikeRepository repository;
    private final UploadMediaPort uploadMediaPort;
    /**
     * Generates signed upload URLs for the requested bike media files.
     */
    @Transactional
    public BikeMediaResult execute(CreateBikeUploadMediaCommand command) {
        AuthenticatedUser currentUser = authenticatedUserPort.getAuthenticatedUser().
                orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        validate(command);

        Bike bike = repository.findByIdAndOwnerId(command.bikeId(), currentUser.userId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        UploadMediaCommand mediaRequest = mapper.toMediaUploadRequest(bike, command);
        List<MediaResult> mediaResult = uploadMediaPort.generateUploadUrls(mediaRequest);
        return mapper.toResult(bike, mediaResult);
    }

    private void validate(CreateBikeUploadMediaCommand command) {
        if (command.mediaFiles() == null || command.mediaFiles().isEmpty()) {
            throw new BadRequestException("bike.upload.media.files.null");
        }
    }
}

