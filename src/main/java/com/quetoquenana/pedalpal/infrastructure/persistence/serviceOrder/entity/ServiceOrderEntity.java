package com.quetoquenana.pedalpal.infrastructure.persistence.serviceOrder.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "service_orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ServiceOrderEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "appointment_id", nullable = false)
    private UUID appointmentId;

    @Column(name = "bike_id", nullable = false)
    private UUID bikeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ServiceOrderStatus status;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ServiceOrderDetailEntity> serviceOrderDetails;

    public void addServiceOrderDetail(ServiceOrderDetailEntity detail) {
        BigDecimal price = detail.getPriceSnapshot() != null ? detail.getPriceSnapshot() : BigDecimal.ZERO;
        if (serviceOrderDetails == null) {
            serviceOrderDetails = new java.util.HashSet<>();
        }
        serviceOrderDetails.add(detail);

        if (this.totalPrice == null) {
            this.totalPrice = BigDecimal.ZERO;
        }

        this.totalPrice = this.totalPrice.add(price);
        detail.setServiceOrder(this);
    }

    public void removeServiceOrderDetail(ServiceOrderDetailEntity detail) {
        if (serviceOrderDetails != null && serviceOrderDetails.remove(detail)) {
            BigDecimal price = detail.getPriceSnapshot() != null ? detail.getPriceSnapshot() : BigDecimal.ZERO;
            this.totalPrice = totalPrice.subtract(price);
            if (this.totalPrice .compareTo(BigDecimal.ZERO) < 0) {
                this.totalPrice = BigDecimal.ZERO;
            }
            detail.setServiceOrder(null);
        }
    }
}
