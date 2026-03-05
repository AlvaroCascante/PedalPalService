package com.quetoquenana.pedalpal.media.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.media.infrastructure.persistence.entity.MediaEntity;
import com.quetoquenana.pedalpal.media.infrastructure.persistence.mapper.MediaEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MediaRepositoryImpl implements MediaRepository {

    private final MediaJpaRepository repository;

    @Override
    public Optional<Media> getById(UUID id) {
        return repository.findById(id).map(MediaEntityMapper::toModel);
    }

    @Override
    public Optional<Media> getByStorageKey(String storageKey) {
        return repository.getByStorageKey(storageKey).map(MediaEntityMapper::toModel);
    }

    @Override
    public List<Media> findByOwnerId(UUID id) {
        return repository.findByOwnerId(id).stream().map(MediaEntityMapper::toModel).toList();
    }

    @Override
    public List<Media> findByReferenceId(UUID id) {
        return repository.findByReferenceId(id).stream().map(MediaEntityMapper::toModel).toList();
    }

    @Override
    public Media save(Media model) {
        MediaEntity entity = MediaEntityMapper.toEntity(model);
        return MediaEntityMapper.toModel(repository.save(entity));
    }
}

