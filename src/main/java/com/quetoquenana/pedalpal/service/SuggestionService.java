package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateSuggestionRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateSuggestionRequest;
import com.quetoquenana.pedalpal.model.data.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SuggestionService {

    Page<Suggestion> findAll(Pageable pageable);

    Suggestion findById(UUID id);

    Suggestion createSuggestion(CreateSuggestionRequest request);

    Suggestion updateSuggestion(UUID id, UpdateSuggestionRequest request);

    void deleteSuggestion(UUID id);

    List<Suggestion> findByBikeId(UUID bikeId);

    List<Suggestion> findBySuggestionType(String code);

    List<Suggestion> searchByName(String name);

    List<Suggestion> findByDateRange(LocalDate start, LocalDate end);
}

