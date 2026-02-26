package com.quetoquenana.pedalpal.systemCode.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
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
    private GeneralStatus status;
    private Integer position;
}
