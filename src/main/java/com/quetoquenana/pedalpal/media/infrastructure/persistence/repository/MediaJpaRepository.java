package com.quetoquenana.pedalpal.media.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.media.infrastructure.persistence.entity.MediaEntity;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaJpaRepository extends JpaRepository<MediaEntity, UUID> {
    List<MediaEntity> findByReferenceIdAndReferenceType(UUID referenceId, MediaReferenceType referenceType);
}
