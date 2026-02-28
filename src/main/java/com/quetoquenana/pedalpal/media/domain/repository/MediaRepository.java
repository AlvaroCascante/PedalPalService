package com.quetoquenana.pedalpal.media.domain.repository;

import com.quetoquenana.pedalpal.media.domain.model.Media;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository {

    Optional<Media> getById(UUID id);

    Optional<Media> getByStorageKey(String storageKey);

    List<Media> findByOwnerId(UUID id);

    List<Media> findByReferenceId(UUID id);

    Media save(Media bike);
}