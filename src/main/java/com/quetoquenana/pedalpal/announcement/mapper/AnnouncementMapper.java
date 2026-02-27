package com.quetoquenana.pedalpal.announcement.mapper;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementMapper {

    public Announcement toModel(CreateAnnouncementCommand command) {
        return Announcement.builder()
                .title(command.title())
                .subTitle(command.subTitle())
                .description(command.description())
                .position(command.position())
                .url(command.url())
                .status(GeneralStatus.ACTIVE)
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
}
