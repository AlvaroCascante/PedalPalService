package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaintenanceSuggestionRequest {

    private String suggestionStatus;
    private String aiProvider;
    private String aiModel;
    private String rawPrompt;
    private String rawResponse;
    private Integer processingAttempts;
    private String lastError;
}
