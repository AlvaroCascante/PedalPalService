package com.quetoquenana.pedalpal.announcement.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.*;

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
    private GeneralStatus status;
}

