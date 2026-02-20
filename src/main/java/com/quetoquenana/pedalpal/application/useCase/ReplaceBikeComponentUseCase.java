package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.domain.model.SystemCode;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.domain.repository.SystemCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReplaceBikeComponentUseCase {

    private final BikeRepository bikeRepository;
    private final SystemCodeRepository systemCodeRepository;

    public BikeResult execute(AddBikeComponentCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        bike.getComponents().stream()
                .filter(c -> c.getId().equals(command.componentId()))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("bike.component.not.found"));

        SystemCode componentType = systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, command.type())
                .orElseThrow(() -> new RecordNotFoundException("bike.component.type.not.found", command.type()));
        try {
            BikeComponent newComponent = BikeMapper.toBikeComponent(command);
            newComponent.setComponentType(componentType);

            bike.removeComponent(command.componentId());
            bike.addComponent(newComponent);

            return BikeMapper.toBikeResult(bikeRepository.save(bike));
        } catch (RuntimeException ex) {
            log.error("RuntimeException on ReplaceBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.replace.component.failed");
        }
    }
}
