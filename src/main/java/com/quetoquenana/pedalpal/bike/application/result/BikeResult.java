package com.quetoquenana.pedalpal.bike.application.result;

import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record BikeResult(
        UUID id,
        String name,
        String type,
        String status,
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

    public Set<BikeComponentResult> getActiveComponents() {
        return components.stream()
                .filter(c -> c.status() == BikeComponentStatus.ACTIVE)
                .collect(Collectors.toSet());
    }

    public Set<BikeComponentResult> getComponents(Set<BikeComponentStatus> statuses) {
        return components.stream()
                .filter(c -> statuses.contains(c.status()))
                .collect(Collectors.toSet());
    }
}
