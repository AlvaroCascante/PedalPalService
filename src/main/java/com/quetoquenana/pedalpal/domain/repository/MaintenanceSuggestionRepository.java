package com.quetoquenana.pedalpal.domain.repository;

import com.quetoquenana.pedalpal.domain.model.MaintenanceSuggestion;

import java.util.Optional;
import java.util.UUID;

public interface MaintenanceSuggestionRepository {

    Optional<MaintenanceSuggestion> getById(UUID maintenanceSuggestionId);

    MaintenanceSuggestion save(MaintenanceSuggestion maintenanceSuggestion);

    MaintenanceSuggestion update(UUID maintenanceSuggestionId, MaintenanceSuggestion maintenanceSuggestion);

    void deleteById(UUID maintenanceSuggestionId);
}

