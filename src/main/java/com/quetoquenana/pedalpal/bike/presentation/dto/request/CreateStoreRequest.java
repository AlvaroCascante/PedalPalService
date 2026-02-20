package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreRequest {

    @NotBlank(message = "{store.create.name.blank}")
    @Size(max = 100, message = "{store.create.name.max}")
    private String name;

    @Size(max = 500, message = "{store.create.description.max}")
    private String description;
}

