package com.quetoquenana.pedalpal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupLogger implements ApplicationRunner {

    private final Environment env;

    public StartupLogger(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("--- Application properties (selected) ---");
        for (String key : getKeys()) {
            if (key == null || key.isBlank()) continue;
            String value = env.getProperty(key);
            if (value == null) {
                log.info("{} = <not set>", key);
            } else {
                log.info("{} = {}", key, value);
            }
        }
        log.info("--- End of properties ---");
    }

    private static String[] getKeys() {
        return new String[] {
                    "strava.client.id",
                    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                    "strava.mobile-callback-deep-link-delay-ms",
                    "strava.mobile-callback-message-delay-ms",
                    "strava.scopes",
                    "spring.datasource.username",
                    "app.support.email"
        };
    }
}
