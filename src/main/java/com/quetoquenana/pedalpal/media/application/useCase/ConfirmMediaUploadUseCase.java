package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.port.CdnUrlProvider;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConfirmMediaUploadUseCase {

    private final MediaRepository repository;
    private final MediaMapper mapper;
    private final CdnUrlProvider cdnUrlProvider;

    public ConfirmedUploadResult execute(ConfirmUploadCommand command) {
        Media model = repository.getById(command.mediaId())
                .orElseThrow(() -> new RecordNotFoundException("media.not.found"));

        if (!model.getStatus().equals(MediaStatus.DRAFT)) {
            throw new BusinessException("media.not.pending");
        }

        model.confirmUploaded();

        // Persist
        repository.save(model);

        String cdnUrl = cdnUrlProvider.providePublic(model.getStorageKey());

        // Return result
        return mapper.toConfirmedResult(model, cdnUrl);
    }
}
