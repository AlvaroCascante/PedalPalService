package com.quetoquenana.pedalpal.maintenanceSuggestion.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceSuggestionItem extends Auditable {

    private UUID id;
    private UUID productsPackageId;
    private UUID productId;
    private String priority;
    private String urgency;
    private String reason;
}

