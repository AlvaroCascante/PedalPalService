package com.quetoquenana.pedalpal.infrastructure.persistence.systemCode.entity;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
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

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private GeneralStatus status;

    @Column(name = "code_key")
    private String codeKey;

    @Column(name = "position")
    private Integer position;
}
