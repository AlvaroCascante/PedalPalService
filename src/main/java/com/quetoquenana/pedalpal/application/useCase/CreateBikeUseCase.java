package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class CreateBikeUseCase {

    private final BikeRepository bikeRepository;

    public BikeResult execute(CreateBikeCommand command) {
        if (command.serialNumber() != null && bikeRepository.existsBySerialNumber(command.serialNumber())) {
            throw new BusinessException("bike.serial.number.already.exists", command.serialNumber());
        }
        try {
            Bike newBike = BikeMapper.toBike(command);
            return BikeMapper.toBikeResult(bikeRepository.save(newBike));
        } catch (RuntimeException ex) {
            log.error("RuntimeException on CreateBikeUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.creation.failed");
        }
    }
}
