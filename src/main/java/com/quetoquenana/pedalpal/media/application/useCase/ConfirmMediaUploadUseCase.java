package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConfirmMediaUploadUseCase {

    private final MediaRepository repository;

    public void execute(ConfirmUploadCommand command) {
        Media model = repository.getById(command.mediaId())
                .orElseThrow(() -> new RecordNotFoundException("media.not.found"));

        if (!model.getStatus().equals(MediaStatus.DRAFT)) {
            throw new BusinessException("media.not.pending");
        }

        model.confirmUploaded();

        repository.save(model);
    }
}
