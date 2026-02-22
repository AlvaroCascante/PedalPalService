package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class SystemCode {
    private UUID id;
    private String category;
    private String code;
    private String label;
    private String description;
    private String codeKey;
    private Boolean isActive;
    private Integer position;
}
