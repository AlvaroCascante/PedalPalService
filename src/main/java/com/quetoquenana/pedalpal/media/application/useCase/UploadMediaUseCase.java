package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.media.application.port.StorageProvider;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.security.application.OwnershipValidator;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.SignedUrl;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.media.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class UploadMediaUseCase {

    private final MediaRepository repository;
    private final MediaMapper mapper;
    private final StorageProvider storageProvider;
    private final OwnershipValidator ownershipValidator;

    public Set<UploadMediaResult> execute(UploadMediaCommand command) {
        // Validate ownership
        ownershipValidator.validate(
                command.referenceType(),
                command.referenceId(),
                command.authenticatedUserId(),
                command.isAdmin()
        );

        // Build models + signedUrl in one pass
        Set<Media> models = command.mediaSpecs().stream()
                .map(spec -> {
                    Media model = mapper.toModel(command, spec);
                    SignedUrl signedUrl = storageProvider.generateUploadUrl(
                            model.getStorageKey(),
                            model.getContentType(),
                            model.getIsPrimary()
                    );
                    model.setSignedUrl(signedUrl);
                    return model;
                })
                .collect(Collectors.toSet());

        // Persist
        models.forEach(repository::save);

        // Return result
        return mapper.toResult(models);
    }
}
