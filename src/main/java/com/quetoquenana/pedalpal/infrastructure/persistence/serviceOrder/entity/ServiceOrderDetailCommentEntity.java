package com.quetoquenana.pedalpal.infrastructure.persistence.serviceOrder.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "service_order_detail_comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServiceOrderDetailCommentEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_order_detail_id", nullable = false)
    @JsonBackReference
    private ServiceOrderDetailEntity serviceOrderDetail;

    @Column(name = "comment", columnDefinition = "text", nullable = false)
    private String comment;

    @Column(name = "image_url")
    private String imageUrl;
}

