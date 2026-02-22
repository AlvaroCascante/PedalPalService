package com.quetoquenana.pedalpal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSystemCodeRequest {

    private String category;

    private String code;

    private String label;

    private String description;

    private Boolean isActive;

    private String codeKey;

    private Integer position;
}

