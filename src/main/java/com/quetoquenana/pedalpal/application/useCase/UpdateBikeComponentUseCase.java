package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.UpdateBikeComponentCommand;
import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
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
public class UpdateBikeComponentUseCase {

    private final BikeRepository bikeRepository;
    private final SystemCodeRepository systemCodeRepository;

    public BikeResult execute(UpdateBikeComponentCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        BikeComponent component = bike.getComponents().stream()
                .filter(c -> c.getId().equals(command.componentId()))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("bike.component.not.found"));

        try {
            applyPatch(component, command);

            return BikeMapper.toBikeResult(bikeRepository.save(bike));
        } catch (RecordNotFoundException ex) {
            log.error("RecordNotFoundException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (BadRequestException ex) {
            log.error("BadRequestException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.component.update.failed");
        }
    }

    private void applyPatch(BikeComponent component, UpdateBikeComponentCommand command) {
        if (command.name() != null) {
            rejectBlank(command.name(), "bike.component.update.name.blank");
            component.setName(command.name());
        }
        if (command.type() != null) {
            rejectBlank(command.type(), "bike.component.update.type.blank");
            SystemCode componentType = systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, command.type())
                    .orElseThrow(() -> new RecordNotFoundException("bike.component.type.not.found", command.type()));
            component.setComponentType(componentType);
        }
        if (command.brand() != null) {
            rejectBlank(command.brand(), "bike.component.update.brand.blank");
            component.setBrand(command.brand());
        }
        if (command.model() != null) {
            rejectBlank(command.model(), "bike.component.update.model.blank");
            component.setModel(command.model());
        }
        if (command.notes() != null) {
            rejectBlank(command.notes(), "bike.component.update.notes.blank");
            component.setNotes(command.notes());
        }
        if (command.odometerKm() != null) {
            component.setOdometerKm(command.odometerKm());
        }
        if (command.usageTimeMinutes() != null) {
            component.setUsageTimeMinutes(command.usageTimeMinutes());
        }
    }

    private void rejectBlank(String value, String messageKey) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException(messageKey);
        }
    }
}
