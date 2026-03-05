package com.quetoquenana.pedalpal.announcement.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.infrastructure.persistence.entity.AnnouncementEntity;

/**
 * Maps announcement persistence entities to domain models and back.
 * Prefer static utility if they are pure and dependency‑free.
 * If they need JPA helpers, converters, or other collaborators,
 * use DI and keep them package‑private when possible.
 */
public class AnnouncementEntityMapper {

    private AnnouncementEntityMapper() {}

    public static AnnouncementEntity toEntity(Announcement model) {
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

    public static Announcement toModel(AnnouncementEntity entity) {
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
