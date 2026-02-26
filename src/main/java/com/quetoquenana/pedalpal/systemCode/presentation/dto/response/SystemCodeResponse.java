package com.quetoquenana.pedalpal.systemCode.presentation.dto.response;

import java.util.UUID;

public record SystemCodeResponse(
        UUID id,
        String code,
        String name,
        String status,
        Integer position
) { }

