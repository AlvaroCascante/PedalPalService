package com.quetoquenana.pedalpal.domain.repository;

import com.quetoquenana.pedalpal.domain.model.CfImage;

import java.util.Optional;
import java.util.UUID;

public interface CfImageRepository {

    Optional<CfImage> getById(UUID cfImageId);

    CfImage save(CfImage cfImageId);

    CfImage update(UUID cfImageId, CfImage cfImage);

    void deleteById(UUID cfImageId);
}

