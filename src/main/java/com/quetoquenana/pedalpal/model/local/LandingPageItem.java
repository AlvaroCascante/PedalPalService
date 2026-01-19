package com.quetoquenana.pedalpal.model.local;

import com.quetoquenana.pedalpal.dto.api.request.CreateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "landing_page_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LandingPageItem extends Auditable {

    public static class LandingList extends ApiBaseResponseView.Always {}
    public static class LandingDetail extends LandingPageItem.LandingList {}

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
    private SystemCode status;

    // Convenience factory to build from DTO
    public static LandingPageItem createFromRequest(CreateLandingItemRequest req) {
        return LandingPageItem.builder()
                .title(req.getTitle())
                .subtitle(req.getSubtitle())
                .description(req.getDescription())
                .position(req.getPosition())
                .url(req.getUrl())
                .build();
    }

    // Update fields from UpdateLandingItemRequest (partial update)
    public void updateFromRequest(UpdateLandingItemRequest req) {
        if (req.getTitle() != null) this.title = req.getTitle();
        if (req.getSubtitle() != null) this.subtitle = req.getSubtitle();
        if (req.getDescription() != null) this.description = req.getDescription();
        if (req.getPosition() != null) this.position = req.getPosition();
        if (req.getUrl() != null) this.url = req.getUrl();
    }

}
