package com.quetoquenana.pedalpal.serviceorder.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain model representing a comment attached to a service order.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderComment {
    private UUID id;
    private UUID serviceOrderId;
    private String comment;
    private Boolean customerVisible;
    private UserType createdByType;
    private Instant createdAt;
    private UUID createdBy;
}
