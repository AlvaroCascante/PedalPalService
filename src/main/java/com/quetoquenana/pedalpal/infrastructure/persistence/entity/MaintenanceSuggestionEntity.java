package com.quetoquenana.pedalpal.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity.BikeEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.systemCode.entity.SystemCodeEntity;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bike_id", nullable = false)
    private BikeEntity bike;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "suggestion_type", nullable = false)
    private SystemCodeEntity suggestionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggestion_status")
    private SystemCodeEntity suggestionStatus;

    @Column(name = "ai_provider", length = 50)
    private String aiProvider;

    @Column(name = "ai_model", length = 100)
    private String aiModel;

    @Column(name = "raw_prompt", columnDefinition = "text")
    private String rawPrompt;

    @Column(name = "raw_response", columnDefinition = "text")
    private String rawResponse;

    @Column(name = "processing_attempts", nullable = false)
    private Integer processingAttempts;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    @Transient
    private List<MaintenanceSuggestionItemEntity> items;
}
