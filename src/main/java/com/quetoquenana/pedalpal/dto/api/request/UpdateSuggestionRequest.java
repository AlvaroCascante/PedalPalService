package com.quetoquenana.pedalpal.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSuggestionRequest {
    private UUID bikeId;
    private String suggestionType;
    private BigDecimal confidenceScore;
    private UUID packageId;
    private UUID productId;
    private String name;
    private String description;
    private LocalDate suggestedDate;
}

