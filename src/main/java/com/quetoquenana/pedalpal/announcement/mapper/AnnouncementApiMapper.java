package com.quetoquenana.pedalpal.announcement.mapper;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementStatusCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.CreateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementStatusRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.response.AnnouncementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AnnouncementApiMapper {

    private final MessageSource messageSource;

    public CreateAnnouncementCommand toCommand(CreateAnnouncementRequest request, UUID authenticatedUserId) {
        return new CreateAnnouncementCommand(
                authenticatedUserId,
                request.title(),
                request.subTitle(),
                request.description(),
                request.position(),
                request.url()
        );
    }

    public UpdateAnnouncementCommand toCommand(UUID id, UpdateAnnouncementRequest request, UUID authenticatedUserId) {
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

    public UpdateAnnouncementStatusCommand toCommand(UUID id, UpdateAnnouncementStatusRequest request) {
        return new UpdateAnnouncementStatusCommand(
                id,
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
                statusLabel
        );
    }
}
