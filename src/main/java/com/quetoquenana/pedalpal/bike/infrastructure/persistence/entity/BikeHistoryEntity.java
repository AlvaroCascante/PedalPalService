package com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bike_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BikeHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "bike_id", nullable = false)
    private UUID bikeId;

    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "performed_by", nullable = false)
    private UUID performedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private BikeHistoryEventType type;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;
}
