package com.quetoquenana.pedalpal.media.domain.repository;

import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository {

    Optional<Media> getById(UUID id);

    List<Media> findByReferenceIdAndReferenceType(UUID referenceId, MediaReferenceType referenceType);

    Media save(Media bike);
}