package com.quetoquenana.pedalpal.common.domain.repository;

import com.quetoquenana.pedalpal.common.domain.model.MaintenanceSuggestion;

import java.util.Optional;
import java.util.UUID;

public interface MaintenanceSuggestionRepository {

    Optional<MaintenanceSuggestion> getById(UUID maintenanceSuggestionId);

    MaintenanceSuggestion save(MaintenanceSuggestion maintenanceSuggestion);

    MaintenanceSuggestion update(UUID maintenanceSuggestionId, MaintenanceSuggestion maintenanceSuggestion);

    void deleteById(UUID maintenanceSuggestionId);
}

