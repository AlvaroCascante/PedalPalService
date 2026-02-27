package com.quetoquenana.pedalpal.infrastructure.persistence.announcement.mapper;

import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.infrastructure.persistence.announcement.entity.AnnouncementEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnnouncementEntityMapper {

    public AnnouncementEntity toEntity(Announcement model) {
        AnnouncementEntity entity = AnnouncementEntity.builder()
                .id(model.getId())
                .title(model.getTitle())
                .subtitle(model.getSubTitle())
                .description(model.getDescription())
                .position(model.getPosition())
                .url(model.getUrl())
                .status(model.getStatus())
                .build();
        entity.setVersion(model.getVersion());
        return entity;
    }

    public Announcement toModel(AnnouncementEntity entity) {
        Announcement domain = Announcement.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .subTitle(entity.getSubtitle())
                .description(entity.getDescription())
                .position(entity.getPosition())
                .url(entity.getUrl())
                .status(entity.getStatus())
                .build();

        domain.setVersion(entity.getVersion());
        return domain;
    }
}
