package com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.entity;

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

    @Column(name = "category", length = 50, nullable = false)
    private String category;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "code_key", length =  100, nullable = false)
    private String codeKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private GeneralStatus status;

    @Column(name = "label", length = 50)
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "position")
    private Integer position;
}
