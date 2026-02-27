package com.quetoquenana.pedalpal.infrastructure.persistence.announcement.entity;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "announcements")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AnnouncementEntity extends AuditableEntity {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GeneralStatus status;
}
