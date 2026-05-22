package com.quetoquenana.pedalpal.media.presentation.mapper;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.response.MediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.UUID;

@RequiredArgsConstructor
public class MediaApiMapper {

    private final MessageSource messageSource;

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
                statusLabel,
                result.name(),
                result.altText(),
                result.url(),
                result.expiresAt(),
                result.isPublic() != null ? result.isPublic() : false
        );
    }

    public UploadMediaCommand toCommand(UUID id, UploadMediaRequest request) {
        return new UploadMediaCommand(
                request.isPublic(), // Assuming media is not public by default
                id,
                MediaReferenceType.from(request.referenceType()), // Convert string to enum
                request.mediaFiles().stream()
                        .map(file -> new UploadMediaSpecCommand(
                                file.contentType(),
                                file.name(),
                                file.altText()
                        ))
                        .toList()
        );
    }
}
