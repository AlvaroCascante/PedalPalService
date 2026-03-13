package com.quetoquenana.pedalpal.strava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Strava REST client configuration.
 */
@Configuration
public class StravaClientConfig {

    /**
     * RestClient configured for Strava API calls.
     */
    @Bean
    public RestClient stravaRestClient(StravaProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getApiBaseUrl())
                .build();
    }
}
