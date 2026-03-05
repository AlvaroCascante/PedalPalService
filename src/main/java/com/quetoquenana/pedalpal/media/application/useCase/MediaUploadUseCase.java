package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.media.application.port.StorageProvider;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.port.OwnershipValidationPort;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.application.model.SignedUrl;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class MediaUploadUseCase {

    private final MediaRepository repository;
    private final MediaMapper mapper;
    private final StorageProvider storageProvider;
    private final OwnershipValidationPort ownershipValidationPort;
    private final String defaultStorageProvider;

    public Set<UploadMediaResult> execute(UploadMediaCommand command) {
        // Validate ownership
        ownershipValidationPort.validate(
                command.referenceType(),
                command.referenceId(),
                command.authenticatedUserId(),
                command.isAdmin()
        );

        // Build models + signedUrl in one pass
        return command.mediaSpecs().stream()
                .map(spec -> {
                    Media model = mapper.toModel(command, spec);
                    SignedUrl signedUrl = storageProvider.generateUploadUrl(
                            model.getStorageKey(),
                            model.getContentType(),
                            command.isPublic()
                    );
                    Media persisted = model.assignProvider(defaultStorageProvider);
                    repository.save(persisted);
                    return mapper.toResult(persisted, signedUrl);
                })
                .collect(Collectors.toSet());
    }
}
