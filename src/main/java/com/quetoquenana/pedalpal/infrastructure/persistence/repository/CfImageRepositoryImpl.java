package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.domain.model.CfImage;
import com.quetoquenana.pedalpal.domain.repository.CfImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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

