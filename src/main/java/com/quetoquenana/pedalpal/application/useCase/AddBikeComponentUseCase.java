package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.domain.model.SystemCode;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.domain.repository.SystemCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;

@Transactional
@RequiredArgsConstructor
public class AddBikeComponentUseCase {

    private final BikeRepository bikeRepository;
    private final SystemCodeRepository systemCodeRepository;

    public BikeResult execute(AddBikeComponentCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        SystemCode componentType = systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, command.type())
                .orElseThrow(() -> new RecordNotFoundException("bike.component.type.not.found", command.type()));

        BikeComponent component = BikeMapper.toBikeComponent(command);
        component.setComponentType(componentType);

        bike.addComponent(component);

        return BikeMapper.toBikeResult(bikeRepository.save(bike));
    }
}
