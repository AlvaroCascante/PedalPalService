package com.quetoquenana.pedalpal.model.local;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class CfImage extends Auditable {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_asset_id", nullable = false)
    private String providerAssetId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    // references system_codes.id
    @Column(name = "context_code", nullable = false)
    private UUID contextCode;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "position", nullable = false)
    private Integer position = 0;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "title")
    private String title;

    @Column(name = "alt_text")
    private String altText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    // Create helper
    public static CfImage createFromRequest(com.quetoquenana.pedalpal.dto.api.request.CreateCfImageRequest req) {
        return CfImage.builder()
                .id(java.util.UUID.randomUUID())
                .provider(req.getProvider())
                .providerAssetId(req.getProviderAssetId())
                .ownerId(req.getOwnerId())
                .contextCode(req.getContextCode())
                .referenceId(req.getReferenceId())
                .position(req.getPosition() == null ? 0 : req.getPosition())
                .isPrimary(req.getIsPrimary() != null && req.getIsPrimary())
                .title(req.getTitle())
                .altText(req.getAltText())
                .metadata(req.getMetadata())
                .build();
    }

    // Update helper
    public void updateFromRequest(com.quetoquenana.pedalpal.dto.api.request.UpdateCfImageRequest req) {
        if (req.getProvider() != null) this.provider = req.getProvider();
        if (req.getProviderAssetId() != null) this.providerAssetId = req.getProviderAssetId();
        if (req.getOwnerId() != null) this.ownerId = req.getOwnerId();
        if (req.getContextCode() != null) this.contextCode = req.getContextCode();
        if (req.getReferenceId() != null) this.referenceId = req.getReferenceId();
        if (req.getPosition() != null) this.position = req.getPosition();
        if (req.getIsPrimary() != null) this.isPrimary = req.getIsPrimary();
        if (req.getTitle() != null) this.title = req.getTitle();
        if (req.getAltText() != null) this.altText = req.getAltText();
        if (req.getMetadata() != null) this.metadata = req.getMetadata();
    }

}
