package com.quetoquenana.pedalpal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLandingItemRequest {
    private String title;
    private String subtitle;
    private String description;
    private Integer position;
    private String url;
    private UUID statusId;
}
