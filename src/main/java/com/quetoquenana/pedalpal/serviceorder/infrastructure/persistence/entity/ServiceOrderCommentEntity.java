package com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.common.domain.model.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity mapped to service_order_comments table.
 */
@Entity
@Table(name = "service_order_comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ServiceOrderCommentEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "service_order_id", nullable = false)
    private UUID serviceOrderId;

    @Column(name = "comment", nullable = false, length = 1000)
    private String comment;

    @Column(name = "customer_visible", nullable = false)
    private Boolean customerVisible;

    @Column(name = "created_by_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserType createdByType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;
}