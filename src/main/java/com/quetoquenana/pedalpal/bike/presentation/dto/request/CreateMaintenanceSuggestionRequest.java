package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceSuggestionRequest {

    @NotNull(message = "{suggestion.create.bike.required}")
    private UUID bikeId;

    @Valid
    @Size(max = 50, message = "{suggestion.items.max}")
    private List<SuggestionItemRequest> items;
}
