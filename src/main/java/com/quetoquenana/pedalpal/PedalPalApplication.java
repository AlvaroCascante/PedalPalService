package com.quetoquenana.pedalpal;

import com.quetoquenana.pedalpal.config.MaintenanceSuggestionJobProperties;
import com.quetoquenana.pedalpal.config.MaintenanceSuggestionPromptProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
		MaintenanceSuggestionJobProperties.class,
		MaintenanceSuggestionPromptProperties.class
})
public class PedalPalApplication {

    public static void main(String[] args) {
		SpringApplication.run(PedalPalApplication.class, args);
	}

}
