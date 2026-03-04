package com.quetoquenana.pedalpal.systemCode.presentation.dto.response;

import java.util.UUID;

public record SystemCodeResponse(
        UUID id,
        String category,
        String code,
        String codeDescription,
        String status,
        Integer position
) { }

