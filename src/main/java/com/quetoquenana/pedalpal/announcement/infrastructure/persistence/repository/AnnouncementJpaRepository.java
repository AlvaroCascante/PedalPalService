package com.quetoquenana.pedalpal.announcement.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.announcement.infrastructure.persistence.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnnouncementJpaRepository extends JpaRepository<AnnouncementEntity, UUID> {

    List<AnnouncementEntity> findByStatusOrderByPositionAscTitleAsc(GeneralStatus status);
}

