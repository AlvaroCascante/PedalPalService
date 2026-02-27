package com.quetoquenana.pedalpal.maintenanceSuggestion.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceSuggestion extends Auditable {

    private UUID id;
    private UUID bikeId;
    private SuggestionType suggestionType;
    private SuggestionStatus status;
    private String aiProvider;
    private String aiModel;
    private String rawPrompt;
    private String rawResponse;
}

