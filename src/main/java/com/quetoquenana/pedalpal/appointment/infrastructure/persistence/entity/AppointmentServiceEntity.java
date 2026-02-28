package com.quetoquenana.pedalpal.appointment.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "appointment_requested_service")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppointmentServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonBackReference
    private AppointmentEntity appointment;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_name_snapshot", length = 50, nullable = false)
    private String productNameSnapshot;

    @Column(name = "price_snapshot", precision = 10, scale = 2, nullable = false)
    private BigDecimal priceSnapshot;
}

