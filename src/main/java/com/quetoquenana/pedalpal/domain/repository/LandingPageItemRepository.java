package com.quetoquenana.pedalpal.domain.repository;

import com.quetoquenana.pedalpal.domain.model.LandingPageItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LandingPageItemRepository {

    Optional<LandingPageItem> getById(UUID landingPageItemId);

    LandingPageItem save(LandingPageItem landingPageItem);

    LandingPageItem update(UUID landingPageItemId, LandingPageItem landingPageItem);

    void deleteById(UUID landingPageItemId);
}

