package com.quetoquenana.pedalpal.announcement.application.mapper;

import com.quetoquenana.pedalpal.announcement.application.command.AnnouncementMediaCommand;
import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.model.AnnouncementStatus;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnouncementMapper {

    public Announcement toModel(CreateAnnouncementCommand command) {
        return Announcement.builder()
                .title(command.title())
                .subTitle(command.subTitle())
                .description(command.description())
                .position(command.position())
                .url(command.url())
                .status(AnnouncementStatus.DRAFT)
                .build();
    }

    public AnnouncementResult toResult(
            Announcement model
    ) {
        return this.toResult(model, Collections.emptyList());
    }

    public AnnouncementResult toResult(
            Announcement model,
            List<MediaResult> mediaResults
    ) {

        return new AnnouncementResult(
                model.getId(),
                model.getTitle(),
                model.getSubTitle(),
                model.getDescription(),
                model.getPosition(),
                model.getUrl(),
                model.getStatus(),
                mediaResults
        );
    }

    public UploadMediaCommand toMediaUploadRequest(Announcement announcement, CreateAnnouncementCommand command) {
        Set<UploadMediaSpecCommand> specs = command.mediaFiles() == null
                ? Collections.emptySet()
                : command.mediaFiles()
                .stream()
                .map(this::toMediaUploadRequest)
                .collect(Collectors.toSet());

        return new UploadMediaCommand(
                true,
                announcement.getId(),
                MediaReferenceType.ANNOUNCEMENT,
                specs
        );
    }

    private UploadMediaSpecCommand toMediaUploadRequest(AnnouncementMediaCommand command) {
        return new UploadMediaSpecCommand(
                command.mediaType(),
                command.contentType(),
                command.isPrimary(),
                command.name(),
                command.altText()
        );
    }

    public void applyUpdate(Announcement model, UpdateAnnouncementCommand command) {
        if (command.title() != null) {
            model.setTitle(command.title());
        }
        if (command.subTitle() != null) {
            model.setSubTitle(command.subTitle());
        }
        if (command.description() != null) {
            model.setDescription(command.description());
        }
        if (command.position() != null) {
            model.setPosition(command.position());
        }
        if (command.url() != null) {
            model.setUrl(command.url());
        }
    }
}
