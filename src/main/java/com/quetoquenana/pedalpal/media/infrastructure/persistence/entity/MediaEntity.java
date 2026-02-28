package com.quetoquenana.pedalpal.media.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.auditing.domain.model.AuditableEntity;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.domain.model.MediaType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
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

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @Column(name = "reference_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private MediaReferenceType referenceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false, length = 50)
    private MediaType mediaType;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MediaStatus status;

    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "provider", nullable = false, length = 50)
    private String provider;

    @Column(name = "provider_asset_id", nullable = false, length = 50)
    private String providerAssetId;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "alt_text")
    private String altText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
}
