package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class CreateBikeUseCase {

    private final BikeRepository bikeRepository;

    public BikeResult execute(CreateBikeCommand command) {
        if (command.serialNumber() != null &&
                bikeRepository.existsBySerialNumber(command.serialNumber())) {
            throw new BusinessException("bike.serial.number.already.exists", command.serialNumber());
        }

        try {
            Bike saved = bikeRepository.save(BikeMapper.toBike(command));
            return BikeMapper.toBikeResult(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("bike.creation.failed", ex);
        }
    }
}
