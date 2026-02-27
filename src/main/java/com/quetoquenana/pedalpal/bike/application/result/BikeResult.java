package com.quetoquenana.pedalpal.bike.application.result;

import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeType;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record BikeResult(
        UUID id,
        String name,
        BikeType type,
        BikeStatus status,
        boolean isPublic,
        boolean isExternalSync,
        String brand,
        String model,
        Integer year,
        String serialNumber,
        String notes,
        int odometerKm,
        int usageTimeMinutes,
        Set<BikeComponentResult> components
) {
    public BikeResult {
        components = (components == null) ? java.util.Set.of() : java.util.Set.copyOf(components);
    }

    public Set<BikeComponentResult> getComponents(Set<BikeComponentStatus> statuses) {
        return components.stream()
                .filter(c -> statuses.contains(c.status()))
                .collect(Collectors.toSet());
    }
}
