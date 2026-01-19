package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.data.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, UUID> {

    List<Suggestion> findByBikeId(UUID bikeId);

    List<Suggestion> findBySuggestionTypeCode(String code);

    List<Suggestion> findByNameContainingIgnoreCase(String name);

    List<Suggestion> findBySuggestedDateBetween(LocalDate start, LocalDate end);
}

