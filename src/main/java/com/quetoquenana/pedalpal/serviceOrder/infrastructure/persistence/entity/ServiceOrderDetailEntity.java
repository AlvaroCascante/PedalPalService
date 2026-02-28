package com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.quetoquenana.pedalpal.auditing.domain.model.AuditableEntity;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderDetailStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "service_order_details")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServiceOrderDetailEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_order_id", nullable = false)
    @JsonBackReference
    private ServiceOrderEntity serviceOrder;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "technician_id")
    private UUID technicianId;

    @Column(name = "product_name_snapshot", length = 50, nullable = false)
    private String productNameSnapshot;

    @Column(name = "price_snapshot", precision = 10, scale = 2)
    private BigDecimal priceSnapshot;

    @Column(name = "status", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceOrderDetailStatus status;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;


    @OneToMany(mappedBy = "serviceOrderDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ServiceOrderDetailCommentEntity> comments;

    public void addComment(ServiceOrderDetailCommentEntity comment) {
        if (comments == null) {
            comments = new java.util.HashSet<>();
        }
        comments.add(comment);
        comment.setServiceOrderDetail(this);
    }
}
