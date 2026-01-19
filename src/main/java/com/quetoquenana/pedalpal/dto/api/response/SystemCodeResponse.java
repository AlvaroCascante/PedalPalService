package com.quetoquenana.pedalpal.dto.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quetoquenana.pedalpal.model.local.SystemCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemCodeResponse {

    private UUID id;
    private String category;
    private String code;
    private String label;
    private String description;
    private Boolean isActive;
    private String codeKey;
    private String keyValue;
    private Integer position;

    public static SystemCodeResponse fromEntity(SystemCode entity, String keyValue) {
        if (entity == null) return null;
        return new SystemCodeResponse(
                entity.getId(),
                entity.getCategory(),
                entity.getCode(),
                entity.getLabel(),
                entity.getDescription(),
                entity.getIsActive(),
                entity.getCodeKey(),
                keyValue,
                entity.getPosition()
        );
    }
}
