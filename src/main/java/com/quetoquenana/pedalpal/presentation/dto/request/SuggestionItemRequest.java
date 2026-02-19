package com.quetoquenana.pedalpal.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionItemRequest {
    private UUID packageId;
    private UUID productId;
    private String priorityCode;
    private String urgencyCode;

    @NotBlank(message = "{suggestion.item.reason.required}")
    private String reason;
}

