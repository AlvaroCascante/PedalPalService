package com.quetoquenana.pedalpal.domain.model;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MaintenanceSuggestionItem {

    private UUID id;

    private MaintenanceSuggestion suggestion;

    private ProductPackage productsPackage;

    private Product product;

    private SystemCode priority;

    private SystemCode urgency;

    private String reason;
}
