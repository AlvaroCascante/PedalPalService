package com.quetoquenana.pedalpal.store.presentation.dto.response;

import java.util.Set;
import java.util.UUID;

public record StoreResponse(
        UUID id,
        String name,
        Set<StoreLocationResponse> locations
) { }
