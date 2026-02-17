package com.quetoquenana.pedalpal.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "landing_page_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LandingPageItemEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "sub_title", length = 200)
    private String subtitle;

    @Column(name = "description")
    private String description;

    @Column(name = "position")
    private Integer position;

    @Column(name = "url", length = 500)
    private String url;

    // status_id references system_codes.id -> stored as a SystemCode relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private SystemCodeEntity status;

}
