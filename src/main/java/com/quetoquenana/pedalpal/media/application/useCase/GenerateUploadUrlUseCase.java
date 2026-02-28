package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.media.application.port.StorageProvider;
import com.quetoquenana.pedalpal.media.application.command.GenerateUploadUrlCommand;
import com.quetoquenana.pedalpal.media.application.result.GenerateUploadUrlResult;
import com.quetoquenana.pedalpal.security.application.OwnershipValidator;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.domain.model.SignedUrl;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.media.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class GenerateUploadUrlUseCase {

    private final MediaRepository repository;
    private final MediaMapper mapper;
    private final StorageProvider storageProvider;
    private final OwnershipValidator ownershipValidator;

    public GenerateUploadUrlResult execute(GenerateUploadUrlCommand command) {
        MediaReferenceType referenceType = MediaReferenceType.from(command.referenceType());

        // Validate ownership
        ownershipValidator.validate(
                referenceType,
                command.ownerId(),
                command.authenticatedUserId(),
                command.isAdmin()
        );

        // Generate storage key
        Media model = mapper.toModel(command);

        // Generate signed URL
        SignedUrl signedUrl = storageProvider.generateUploadUrl(
                model.getStorageKey(),
                command.contentType(),
                referenceType.isPublic()
        );

        // Persist
        repository.save(model);

        // Return result
        return mapper.toResult(model, signedUrl);
    }
}
