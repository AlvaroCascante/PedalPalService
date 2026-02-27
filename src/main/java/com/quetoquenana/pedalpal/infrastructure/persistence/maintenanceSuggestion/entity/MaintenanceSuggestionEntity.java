package com.quetoquenana.pedalpal.infrastructure.persistence.maintenanceSuggestion.entity;

import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import com.quetoquenana.pedalpal.maintenanceSuggestion.domain.model.SuggestionStatus;
import com.quetoquenana.pedalpal.maintenanceSuggestion.domain.model.SuggestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "maintenance_suggestions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MaintenanceSuggestionEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "bike_id", nullable = false)
    private UUID bikeId;

    @Column(name = "suggestion_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private SuggestionType suggestionType;

    @Column(name = "status", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private SuggestionStatus status;

    @Column(name = "ai_provider", length = 50)
    private String aiProvider;

    @Column(name = "ai_model", length = 100)
    private String aiModel;

    @Column(name = "raw_prompt", columnDefinition = "text")
    private String rawPrompt;

    @Column(name = "raw_response", columnDefinition = "text")
    private String rawResponse;

    @Transient
    private List<MaintenanceSuggestionItemEntity> items;
}
