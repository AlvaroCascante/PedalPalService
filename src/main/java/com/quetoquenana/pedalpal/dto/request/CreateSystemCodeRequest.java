package com.quetoquenana.pedalpal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSystemCodeRequest {

    @NotBlank(message = "{systemcode.create.category.blank}")
    private String category;

    @NotBlank(message = "{systemcode.create.code.blank}")
    private String code;

    private String label;

    private String description;

    @NotNull(message = "{systemcode.create.isActive.required}")
    private Boolean isActive = true;

    private String codeKey;

    private Integer position;
}

