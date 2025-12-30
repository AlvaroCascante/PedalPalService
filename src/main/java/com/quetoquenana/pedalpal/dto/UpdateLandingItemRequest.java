package com.quetoquenana.pedalpal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLandingItemRequest {
    private String title;
    private String subtitle;
    private String description;
    private String category;
    private String status;
    private Instant startAt;
    private Instant endAt;
    private String imageUrl;
    private String linkUrl;
    private Integer priority;
    private String metadata;
}

