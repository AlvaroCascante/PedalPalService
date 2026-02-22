package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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
