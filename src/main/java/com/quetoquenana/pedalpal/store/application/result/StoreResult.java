package com.quetoquenana.pedalpal.store.application.result;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record StoreResult(
    UUID id,
    String name,
    Set<StoreLocationResult> locations
) {
}
