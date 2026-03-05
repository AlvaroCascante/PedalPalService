package com.quetoquenana.pedalpal.announcement.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Announcement extends Auditable {

    private UUID id;
    private String title;
    private String subTitle;
    private String description;
    private Integer position;
    private String url;
    private AnnouncementStatus status;

    public void activate() {
        this.status = AnnouncementStatus.ACTIVE;
    }

    public void inactivate() {
        this.status = AnnouncementStatus.INACTIVE;
    }

    // Equality based on title and subtitle, as they are the main identifying features of an announcement
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Announcement that)) return false;
        return Objects.equals(title, that.title) && Objects.equals(subTitle, that.subTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subTitle);
    }
}

