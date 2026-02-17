package com.quetoquenana.pedalpal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai.maintenance.prompt")
public class MaintenanceSuggestionPromptProperties {

    private String version;
    private String system;
    private String user;
}
