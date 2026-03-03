package com.quetoquenana.pedalpal.announcement.mapper;

import com.quetoquenana.pedalpal.announcement.application.command.AnnouncementMediaCommand;
import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementStatusCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.AnnouncementMediaRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.CreateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementStatusRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.response.AnnouncementResponse;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.media.presentation.dto.response.UploadMediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AnnouncementApiMapper {

    private final MessageSource messageSource;

    public CreateAnnouncementCommand toCommand(
            UUID authenticatedUserId,
            boolean isAdmin,
            CreateAnnouncementRequest request
    ) {
        return new CreateAnnouncementCommand(
                authenticatedUserId,
                isAdmin,
                request.title(),
                request.subTitle(),
                request.description(),
                request.position(),
                request.url(),
                request.mediaFiles()
                        .stream()
                        .map(this::toCommand).toList()
        );
    }

    private AnnouncementMediaCommand toCommand(AnnouncementMediaRequest request) {
        return new AnnouncementMediaCommand(
                request.contentType(),
                request.mediaType(),
                request.isPrimary(),
                request.title(),
                request.altText()
        );
    }

    public UpdateAnnouncementCommand toCommand(UUID id, UUID authenticatedUserId, UpdateAnnouncementRequest request) {
        return new UpdateAnnouncementCommand(
                id,
                authenticatedUserId,
                request.title(),
                request.subTitle(),
                request.description(),
                request.position(),
                request.url()
        );
    }

    public UpdateAnnouncementStatusCommand toCommand(UUID id, UUID authenticatedUserId, UpdateAnnouncementStatusRequest request) {
        return new UpdateAnnouncementStatusCommand(
                id,
                authenticatedUserId,
                request.status()
        );
    }

    public AnnouncementResponse toResponse(AnnouncementResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new AnnouncementResponse(
                result.id(),
                result.title(),
                result.subTitle(),
                result.description(),
                result.position(),
                result.url(),
                statusLabel,
                result.uploadMediaResults().stream().map(this::toResponse).collect(Collectors.toSet()));
    }

    private UploadMediaResponse toResponse(UploadMediaResult result) {
        return new UploadMediaResponse(
                result.mediaId(),
                result.storageKey(),
                result.uploadUrl(),
                result.expiresAt()
        );
    }
}
