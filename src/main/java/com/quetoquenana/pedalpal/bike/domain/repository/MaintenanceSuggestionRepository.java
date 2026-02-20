package com.quetoquenana.pedalpal.bike.domain.repository;

import com.quetoquenana.pedalpal.bike.domain.model.MaintenanceSuggestion;

import java.util.Optional;
import java.util.UUID;

public interface MaintenanceSuggestionRepository {

    Optional<MaintenanceSuggestion> getById(UUID maintenanceSuggestionId);

    MaintenanceSuggestion save(MaintenanceSuggestion maintenanceSuggestion);

    MaintenanceSuggestion update(UUID maintenanceSuggestionId, MaintenanceSuggestion maintenanceSuggestion);

    void deleteById(UUID maintenanceSuggestionId);
}

