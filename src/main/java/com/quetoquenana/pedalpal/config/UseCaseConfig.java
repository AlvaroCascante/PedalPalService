package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.application.useCase.*;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.domain.repository.SystemCodeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public AddBikeComponentUseCase createAddBikeComponentUseCase(
            BikeRepository bikeRepository,
            SystemCodeRepository systemCodeRepository
    ) {
        return new AddBikeComponentUseCase(bikeRepository, systemCodeRepository);
    }

    @Bean
    public CreateBikeUseCase createBikeUseCase(BikeRepository bikeRepository) {
        return new CreateBikeUseCase(bikeRepository);
    }

    @Bean
    public ReplaceBikeComponentUseCase createReplaceBikeComponentUseCase(
            BikeRepository bikeRepository,
            SystemCodeRepository systemCodeRepository
            ) {
        return new ReplaceBikeComponentUseCase(bikeRepository, systemCodeRepository);
    }

    @Bean
    public UpdateBikeStatusUseCase createUpdateBikeStatusUseCase(BikeRepository bikeRepository) {
        return new UpdateBikeStatusUseCase(bikeRepository);
    }

    @Bean
    public UpdateBikeUseCase createUpdateBikeUseCase(BikeRepository bikeRepository) {
        return new UpdateBikeUseCase(bikeRepository);
    }

    @Bean
    public UpdateBikeComponentUseCase createUpdateBikeComponentUseCase(
            BikeRepository bikeRepository,
            SystemCodeRepository systemCodeRepository
    ) {
        return new UpdateBikeComponentUseCase(bikeRepository, systemCodeRepository);
    }

    @Bean
    public UpdateBikeComponentStatusUseCase createUpdateBikeComponentStatusUseCase(BikeRepository bikeRepository) {
        return new UpdateBikeComponentStatusUseCase(bikeRepository);
    }
}