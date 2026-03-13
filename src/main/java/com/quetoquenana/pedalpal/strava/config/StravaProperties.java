package com.quetoquenana.pedalpal.strava.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for Strava integration.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "strava")
public class StravaProperties {

    /** Strava OAuth base URL. */
    private String oauthBaseUrl;

    /** Strava API base URL. */
    private String apiBaseUrl;

    /** OAuth client id. */
    private String clientId;

    /** OAuth client secret. */
    private String clientSecret;

    /** OAuth redirect URI. */
    private String redirectUri;

    /** OAuth scopes list. */
    private String scopes;

    /** Webhook verification token. */
    private String webhookVerifyToken;

    /** Token refresh skew in seconds. */
    private long tokenRefreshSkewSeconds = 60;
}
