package com.quetoquenana.pedalpal.dto.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSuggestionRequest {

    @NotNull(message = "{suggestion.create.bike.required}")
    private UUID bikeId;

    @NotNull(message = "{suggestion.create.type.required}")
    private String suggestionType;

    private BigDecimal confidenceScore;

    private UUID packageId;
    private UUID productId;

    @NotBlank(message = "{suggestion.create.name.blank}")
    private String name;

    @NotBlank(message = "{suggestion.create.description.blank}")
    @Size(max = 2000, message = "{suggestion.create.description.max}")
    private String description;

    private LocalDate suggestedDate;
}

