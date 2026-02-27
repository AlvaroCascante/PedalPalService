package com.quetoquenana.pedalpal.maintenanceSuggestion.domain.repository;

import com.quetoquenana.pedalpal.maintenanceSuggestion.domain.model.MaintenanceSuggestion;

import java.util.Optional;
import java.util.UUID;

public interface MaintenanceSuggestionRepository {
    MaintenanceSuggestion save(MaintenanceSuggestion appointment);

    Optional<MaintenanceSuggestion> getById(UUID id);
}

