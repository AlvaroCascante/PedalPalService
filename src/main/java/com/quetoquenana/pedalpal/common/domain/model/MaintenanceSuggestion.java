package com.quetoquenana.pedalpal.common.domain.model;

import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class MaintenanceSuggestion extends Auditable {

    private UUID id;

    private Bike bike;

    private SystemCode suggestionType;

    private SystemCode suggestionStatus;

    private String aiProvider;

    private String aiModel;

    private String rawPrompt;

    private String rawResponse;

    private Integer processingAttempts;

    private String lastError;

    private List<MaintenanceSuggestionItem> items;
}
