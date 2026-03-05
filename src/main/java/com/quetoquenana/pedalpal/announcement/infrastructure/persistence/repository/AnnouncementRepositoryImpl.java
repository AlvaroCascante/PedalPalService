package com.quetoquenana.pedalpal.announcement.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.announcement.infrastructure.persistence.entity.AnnouncementEntity;
import com.quetoquenana.pedalpal.announcement.infrastructure.persistence.mapper.AnnouncementEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AnnouncementRepositoryImpl implements AnnouncementRepository {

    private final AnnouncementJpaRepository jpaRepository;

    @Override
    public Announcement save(Announcement announcement) {
        AnnouncementEntity entity = AnnouncementEntityMapper.toEntity(announcement);
        AnnouncementEntity saved = jpaRepository.save(entity);
        return AnnouncementEntityMapper.toModel(saved);
    }

    @Override
    public Optional<Announcement> getById(UUID id) {
        return jpaRepository.findById(id).map(AnnouncementEntityMapper::toModel);
    }

    @Override
    public List<Announcement> getActive() {
        return jpaRepository.findByStatusOrderByPositionAscTitleAsc(GeneralStatus.ACTIVE)
                .stream()
                .map(AnnouncementEntityMapper::toModel)
                .toList();
    }
}

