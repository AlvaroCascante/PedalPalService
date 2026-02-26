package com.quetoquenana.pedalpal.systemCode.application.result;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SystemCodeResult(
    UUID id,
    String category,
    String code,
    String label,
    String description,
    String codeKey,
    GeneralStatus status,
    Integer position
) {
}
