package com.quetoquenana.pedalpal.infrastructure.persistence.media.entity;

import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MediaEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "provider", length = 50, nullable = false)
    private String provider;

    @Column(name = "provider_asset_id", length = 50, nullable = false)
    private String providerAssetId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "context_code", length = 50, nullable = false)
    private String contextCode;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "alt_text")
    private String altText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
}
