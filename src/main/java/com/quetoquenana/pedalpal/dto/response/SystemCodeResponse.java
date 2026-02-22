package com.quetoquenana.pedalpal.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

}
