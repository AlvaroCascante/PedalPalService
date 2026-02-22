package com.quetoquenana.pedalpal.common.domain.repository;

import com.quetoquenana.pedalpal.common.domain.model.MaintenanceSuggestionItem;

import java.util.Optional;
import java.util.UUID;

public interface MaintenanceSuggestionItemRepository {

    Optional<MaintenanceSuggestionItem> getById(UUID maintenanceSuggestionItemId);

    MaintenanceSuggestionItem save(MaintenanceSuggestionItem maintenanceSuggestionItem);

    MaintenanceSuggestionItem update(UUID maintenanceSuggestionItemId, MaintenanceSuggestionItem maintenanceSuggestionItem);

    void deleteById(UUID maintenanceSuggestionItemId);
}

