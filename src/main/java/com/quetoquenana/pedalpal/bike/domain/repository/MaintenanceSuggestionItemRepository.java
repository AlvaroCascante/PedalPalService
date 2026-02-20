package com.quetoquenana.pedalpal.bike.domain.repository;

import com.quetoquenana.pedalpal.bike.domain.model.MaintenanceSuggestionItem;

import java.util.Optional;
import java.util.UUID;

public interface MaintenanceSuggestionItemRepository {

    Optional<MaintenanceSuggestionItem> getById(UUID maintenanceSuggestionItemId);

    MaintenanceSuggestionItem save(MaintenanceSuggestionItem maintenanceSuggestionItem);

    MaintenanceSuggestionItem update(UUID maintenanceSuggestionItemId, MaintenanceSuggestionItem maintenanceSuggestionItem);

    void deleteById(UUID maintenanceSuggestionItemId);
}

