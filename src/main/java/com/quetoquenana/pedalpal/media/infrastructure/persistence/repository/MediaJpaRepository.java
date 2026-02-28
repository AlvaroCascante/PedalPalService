package com.quetoquenana.pedalpal.media.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.media.infrastructure.persistence.entity.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaJpaRepository extends JpaRepository<MediaEntity, UUID> {
    Optional<MediaEntity> getByStorageKey(String storageKey);
    List<MediaEntity> findByOwnerId(UUID id);
    List<MediaEntity> findByReferenceId(UUID id);
}

