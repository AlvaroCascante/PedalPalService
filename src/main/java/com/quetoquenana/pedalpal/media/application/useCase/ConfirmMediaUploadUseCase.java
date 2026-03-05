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
        Media model = repository.getByStorageKey(command.storageKey())
                .orElseThrow(() -> new RecordNotFoundException("media.not-found"));

        if (model.getStatus() != MediaStatus.DRAFT) {
            throw new BusinessException("media.not.pending");
        }

        Media confirmed = model.confirmUploaded(
                command.providerAssetId(),
                command.sizeBytes(),
                command.metadata()
        );

        // Persist
        repository.save(confirmed);

        String cdnUrl = cdnUrlProvider.providePublic(confirmed.getStorageKey());

        // Return result
        return mapper.toConfirmedResult(confirmed, cdnUrl);
    }
}
