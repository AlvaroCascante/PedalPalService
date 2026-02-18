package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.application.useCase.CreateBikeUseCase;
import com.quetoquenana.pedalpal.application.useCase.UpdateBikeStatusUseCase;
import com.quetoquenana.pedalpal.application.useCase.UpdateBikeUseCase;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateBikeUseCase createBikeUseCase(BikeRepository bikeRepository) {
        return new CreateBikeUseCase(bikeRepository);
    }

    @Bean
    public UpdateBikeUseCase updateBikeUseCase(BikeRepository bikeRepository) {
        return new UpdateBikeUseCase(bikeRepository);
    }

    @Bean
    public UpdateBikeStatusUseCase updateBikeStatusUseCase(BikeRepository bikeRepository) {
        return new UpdateBikeStatusUseCase(bikeRepository);
    }
}