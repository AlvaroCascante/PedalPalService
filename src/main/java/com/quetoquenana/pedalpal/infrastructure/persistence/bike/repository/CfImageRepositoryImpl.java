package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.common.domain.model.CfImage;
import com.quetoquenana.pedalpal.common.domain.repository.CfImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CfImageRepositoryImpl implements CfImageRepository {

    private final CfImageJpaRepository repository;

    @Override
    public Optional<CfImage> getById(UUID cfImageId) {
        return Optional.empty();
    }

    @Override
    public CfImage save(CfImage cfImageId) {
        return null;
    }

    @Override
    public CfImage update(UUID cfImageId, CfImage cfImage) {
        return null;
    }

    @Override
    public void deleteById(UUID cfImageId) {

    }
}

