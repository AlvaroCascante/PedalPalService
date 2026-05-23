package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaSpecCommand;
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

        if (command.referenceType().isUnique()) {
            if (command.mediaSpecs().isEmpty()) {
                return List.of();
            }

            if (command.mediaSpecs().size() > 1) {
                log.info("Unique reference type {} received {} media specs; only the first will be processed",
                        command.referenceType(),
                        command.mediaSpecs().size());
            }

            UploadMediaSpecCommand spec = command.mediaSpecs().getFirst();
            Media model = repository.findByReferenceIdAndReferenceType(command.referenceId(), command.referenceType())
                    .stream()
                    .findFirst()
                    .map(existing -> mapper.toUpdatedModel(
                            existing,
                            currentUser.userId(),
                            command,
                            spec,
                            defaultStorageProvider))
                    .orElseGet(() -> mapper.toModel(
                            currentUser.userId(),
                            command,
                            spec,
                            defaultStorageProvider));

            SignedUrl signedUrl = mediaUrlProvider.generateUploadUrl(
                    model.getStorageKey(),
                    model.getContentType().getContentType(),
                    command.isPublic()
            );
            repository.save(model);
            return List.of(mapper.toResult(model, signedUrl, spec.id()));
        }

        // Build models + signedUrl in one pass
        return command.mediaSpecs().stream()
                .map(spec -> {
                    Media model = mapper.toModel(currentUser.userId(), command, spec, defaultStorageProvider);
                    SignedUrl signedUrl = mediaUrlProvider.generateUploadUrl(
                            model.getStorageKey(),
                            model.getContentType().getContentType(),
                            command.isPublic()
                    );
                    repository.save(model);
                    return mapper.toResult(model, signedUrl, spec.id());
                })
                .toList();
    }
}
