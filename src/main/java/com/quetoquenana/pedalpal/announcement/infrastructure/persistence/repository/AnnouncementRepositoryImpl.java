package com.quetoquenana.pedalpal.announcement.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.announcement.infrastructure.persistence.entity.AnnouncementEntity;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AnnouncementRepositoryImpl implements AnnouncementRepository {

    private final AnnouncementJpaRepository jpaRepository;
    private final AnnouncementEntityMapper mapper;

    @Override
    public Announcement save(Announcement announcement) {
        AnnouncementEntity entity = mapper.toEntity(announcement);
        AnnouncementEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<Announcement> getById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Announcement> getActive() {
        return jpaRepository.findByStatusOrderByPositionAscTitleAsc(GeneralStatus.ACTIVE)
                .stream()
                .map(mapper::toModel)
                .toList();
    }
}

