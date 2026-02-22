package com.quetoquenana.pedalpal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionItemResponse {

    private UUID id;

    private UUID packageId;

    private UUID productId;

    private String priorityCode;

    private String urgencyCode;

    private String reason;
}

