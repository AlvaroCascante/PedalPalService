package com.quetoquenana.pedalpal.media.presentation.mapper;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaSpecRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.response.MediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MediaApiMapper {

    private final MessageSource messageSource;

    public UploadMediaCommand toCommand(
            UploadMediaRequest request
    ) {
        return new UploadMediaCommand(
                request.isPublic(),
                request.referenceId(),
                MediaReferenceType.from(request.referenceType()),
                request.mediaSpecs().stream().map(this::toCommand).collect(Collectors.toSet())
        );
    }

    private UploadMediaSpecCommand toCommand(UploadMediaSpecRequest request) {
        return new UploadMediaSpecCommand(
                request.mediaType(),
                request.contentType(),
                request.isPrimary(),
                request.name(),
                request.altText()
        );
    }

    public ConfirmUploadCommand toCommand(UUID mediaId) {
        return new ConfirmUploadCommand(mediaId);
    }

    public MediaResponse toResponse(MediaResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new MediaResponse(
                result.id(),
                result.contentType(),
                result.provider(),
                result.isPrimary(),
                statusLabel,
                result.name(),
                result.altText(),
                result.url(),
                result.expiresAt()
        );
    }
}
