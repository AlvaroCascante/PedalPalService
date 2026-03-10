package com.quetoquenana.pedalpal.media.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.auditing.domain.model.AuditableEntity;
import com.quetoquenana.pedalpal.media.domain.model.MediaContentType;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "media")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MediaEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @Column(name = "reference_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private MediaReferenceType referenceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false, length = 50)
    private MediaContentType contentType;

    @Column(name = "provider", nullable = false, length = 50)
    private String provider;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MediaStatus status;

    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "alt_text")
    private String altText;
}
