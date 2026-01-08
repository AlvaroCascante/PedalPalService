package com.quetoquenana.pedalpal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "landing_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LandingItem extends Auditable {

    public static class LandingList extends ApiBaseResponseView.Always {}
    public static class LandingDetail extends LandingItem.LandingList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "subtitle", length = 200)
    private String subtitle;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, columnDefinition = "text")
    private LandingCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "text")
    private LandingStatus status;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "link_url", length = 500)
    private String linkUrl;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    // Convenience factory to build from DTO
    public static LandingItem createFromRequest(com.quetoquenana.pedalpal.dto.CreateLandingItemRequest req) {
        return LandingItem.builder()
                .title(req.getTitle())
                .subtitle(req.getSubtitle())
                .description(req.getDescription())
                .category(LandingCategory.fromString(req.getCategory()))
                .status(LandingStatus.fromString(req.getStatus()))
                .startAt(req.getStartAt())
                .endAt(req.getEndAt())
                .imageUrl(req.getImageUrl())
                .linkUrl(req.getLinkUrl())
                .priority(req.getPriority() == null ? 0 : req.getPriority())
                .metadata(req.getMetadata())
                .build();
    }

    // Update fields from UpdateLandingItemRequest (partial update)
    public void updateFromRequest(com.quetoquenana.pedalpal.dto.UpdateLandingItemRequest req) {
        if (req.getTitle() != null) this.title = req.getTitle();
        if (req.getSubtitle() != null) this.subtitle = req.getSubtitle();
        if (req.getDescription() != null) this.description = req.getDescription();
        if (req.getCategory() != null) this.category = LandingCategory.fromString(req.getCategory());
        if (req.getStatus() != null) this.status = LandingStatus.fromString(req.getStatus());
        if (req.getStartAt() != null) this.startAt = req.getStartAt();
        if (req.getEndAt() != null) this.endAt = req.getEndAt();
        if (req.getImageUrl() != null) this.imageUrl = req.getImageUrl();
        if (req.getLinkUrl() != null) this.linkUrl = req.getLinkUrl();
        if (req.getPriority() != null) this.priority = req.getPriority();
        if (req.getMetadata() != null) this.metadata = req.getMetadata();
    }

}
