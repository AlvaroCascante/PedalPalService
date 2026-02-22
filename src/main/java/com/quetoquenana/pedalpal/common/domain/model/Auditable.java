package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class Auditable {

    private Long version;

    private LocalDateTime createdAt;

    private UUID createdBy;

    private LocalDateTime updatedAt;

    private UUID updatedBy;
}
