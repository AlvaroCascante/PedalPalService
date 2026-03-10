package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.application.model.SignedUrl;
import com.quetoquenana.pedalpal.media.application.port.MediaOwnershipValidationPort;
import com.quetoquenana.pedalpal.media.application.port.MediaUrlProvider;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class MediaUploadUseCase {

    private final MediaRepository repository;
    private final MediaMapper mapper;
    private final MediaUrlProvider mediaUrlProvider;
    private final MediaOwnershipValidationPort mediaOwnershipValidationPort;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final String defaultStorageProvider;

    public List<MediaResult> execute(UploadMediaCommand command) {
        AuthenticatedUser currentUser = authenticatedUserPort.getAuthenticatedUser().
                orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

       // Validate ownership
        mediaOwnershipValidationPort.validate(
                command.referenceType(),
                command.referenceId()
        );

        // Build models + signedUrl in one pass
        return command.mediaSpecs().stream()
                .map(spec -> {
                    Media model = mapper.toModel(currentUser.userId(), command, spec);
                    SignedUrl signedUrl = mediaUrlProvider.generateUploadUrl(
                            model.getStorageKey(),
                            model.getContentType().getContentType(),
                            command.isPublic()
                    );
                    Media persisted = model.assignProvider(defaultStorageProvider);
                    repository.save(persisted);
                    return mapper.toResult(persisted, signedUrl);
                })
                .toList();
    }
}
