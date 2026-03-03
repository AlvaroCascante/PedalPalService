package com.quetoquenana.pedalpal.announcement.mapper;

import com.quetoquenana.pedalpal.announcement.application.command.AnnouncementMediaCommand;
import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AnnouncementMapper {

    public Announcement toModel(CreateAnnouncementCommand command) {
        return Announcement.builder()
                .title(command.title())
                .subTitle(command.subTitle())
                .description(command.description())
                .position(command.position())
                .url(command.url())
                .status(GeneralStatus.DRAFT)
                .build();
    }

    public AnnouncementResult toResult(Announcement model) {
        return AnnouncementResult.builder()
                .id(model.getId())
                .title(model.getTitle())
                .subTitle(model.getSubTitle())
                .description(model.getDescription())
                .position(model.getPosition())
                .url(model.getUrl())
                .status(model.getStatus())
                .build();
    }

    public AnnouncementResult toResult(Announcement model, Set<UploadMediaResult> mediaUploadResponse) {

        return AnnouncementResult.builder()
                .id(model.getId())
                .title(model.getTitle())
                .subTitle(model.getSubTitle())
                .description(model.getDescription())
                .position(model.getPosition())
                .url(model.getUrl())
                .status(model.getStatus())
                .uploadMediaResults(mediaUploadResponse.stream().map(this::toResult).collect(Collectors.toSet()))
                .build();
    }

    public UploadMediaResult toResult(UploadMediaResult mediaUploadResponse) {
        return new UploadMediaResult(
                mediaUploadResponse.mediaId(),
                mediaUploadResponse.storageKey(),
                mediaUploadResponse.uploadUrl(),
                mediaUploadResponse.expiresAt()
        );
    }

    public void applyPatch(Announcement model, UpdateAnnouncementCommand command) {
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

    public UploadMediaCommand toMediaUploadRequest(Announcement announcement, CreateAnnouncementCommand command) {
        return new UploadMediaCommand(
                command.authenticatedUserId(),
                command.isAdmin(),
                announcement.getId(),
                MediaReferenceType.ANNOUNCEMENT,
                command.mediaFiles()
                        .stream()
                        .map(this::toMediaUploadRequest)
                        .collect(Collectors.toSet())
        );
    }

    private UploadMediaSpecCommand toMediaUploadRequest(AnnouncementMediaCommand spec) {
        return new UploadMediaSpecCommand(
                spec.contentType(),
                spec.mediaType(),
                spec.isPrimary(),
                spec.title(),
                spec.altText()
        );
    }
}
