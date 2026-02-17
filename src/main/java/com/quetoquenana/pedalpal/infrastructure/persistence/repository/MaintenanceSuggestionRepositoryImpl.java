package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.domain.model.MaintenanceSuggestion;
import com.quetoquenana.pedalpal.domain.repository.MaintenanceSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MaintenanceSuggestionRepositoryImpl implements MaintenanceSuggestionRepository {

    private final MaintenanceSuggestionJpaRepository repository;

    @Override
    public Optional<MaintenanceSuggestion> getById(UUID maintenanceSuggestionId) {
        return Optional.empty();
    }

    @Override
    public MaintenanceSuggestion save(MaintenanceSuggestion maintenanceSuggestion) {
        return null;
    }

    @Override
    public MaintenanceSuggestion update(UUID maintenanceSuggestionId, MaintenanceSuggestion maintenanceSuggestion) {
        return null;
    }

    @Override
    public void deleteById(UUID maintenanceSuggestionId) {

    }
}

