package com.quetoquenana.pedalpal.presentation.dto.api.request;

public record ComponentAiContext(
        String type,
        String name,
        String brand,
        String model
) {}