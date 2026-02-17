package com.quetoquenana.pedalpal.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "system_codes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SystemCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "label")
    private String label;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "position")
    private Integer position;
}
