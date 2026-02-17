package com.quetoquenana.pedalpal.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static com.quetoquenana.pedalpal.common.util.Constants.Roles.SYSTEM;
import static com.quetoquenana.pedalpal.common.util.Constants.SystemCodes.SUGGESTION_TYPE_IA;

/**
 * Configuration properties for the maintenance suggestions job.
 * Bound to properties with prefix "jobs.maintenance-suggestions".
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jobs.maintenance-suggestions")
public class MaintenanceSuggestionJobProperties {

    /** Whether the scheduled job is enabled. */
    private boolean enabled = false;

    /** Cron expression for the scheduled run. */
    private String cron = "";

    /** Page size for paging through bikes. */
    @Min(1)
    private int pageSize = 100;

    /** Suggestion type code to use when creating automatic suggestions. */
    private String suggestionTypeCode = SUGGESTION_TYPE_IA;

    /** Default priority code to set on generated suggestion items (optional). */
    private String priorityCode;

    /** Default urgency code to set on generated suggestion items (optional). */
    private String urgencyCode;

    /** Optional list of component type codes to include; empty = include all. */
    private List<String> componentTypeFilter = new ArrayList<>();

    /** Username to use for audit fields when the job creates suggestions. */
    private String systemUsername = SYSTEM;
}
