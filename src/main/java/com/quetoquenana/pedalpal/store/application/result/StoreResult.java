package com.quetoquenana.pedalpal.store.application.result;

import java.util.Set;
import java.util.UUID;

public record StoreResult(
    UUID id,
    String name,
    Set<StoreLocationResult> locations
) {
}
