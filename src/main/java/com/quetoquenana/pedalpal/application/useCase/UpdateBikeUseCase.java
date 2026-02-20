package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.UpdateBikeCommand;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateBikeUseCase {

    private final BikeRepository bikeRepository;

    public BikeResult execute(UpdateBikeCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));
        try {
            applyPatch(bike, command);

            return BikeMapper.toBikeResult(bikeRepository.save(bike));
        } catch (BadRequestException ex) {
            log.error("BadRequestException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateBikeUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.update.failed");
        }
    }

    private void applyPatch(Bike bike, UpdateBikeCommand command) {
        if (command.name() != null) {
            rejectBlank(command.name(), "bike.update.name.blank");
            bike.setName(command.name());
        }

        if (command.type() != null) {
            rejectBlank(command.type(), "bike.update.type.blank");
            bike.setType(BikeType.from(command.type()));
        }

        if (command.brand() != null) {
            rejectBlank(command.brand(), "bike.update.brand.blank");
            bike.setBrand(command.brand());
        }

        if (command.model() != null) {
            rejectBlank(command.model(), "bike.update.model.blank");
            bike.setModel(command.model());
        }

        if (command.year() != null) {
            bike.setYear(command.year());
        }

        if (command.serialNumber() != null) {
            String newSerial = command.serialNumber();
            if (!Objects.equals(newSerial, bike.getSerialNumber()) && bikeRepository.existsBySerialNumber(newSerial)) {
                throw new BusinessException("bike.serial.number.already.exists", newSerial);
            }
            rejectBlank(command.serialNumber(), "bike.update.serial.blank");
            bike.setSerialNumber(command.serialNumber());
        }

        if (command.notes() != null) {
            rejectBlank(command.notes(), "bike.update.notes.blank");
            bike.setNotes(command.notes());
        }

        if (command.odometerKm() != null) {
            bike.setOdometerKm(command.odometerKm());
        }

        if (command.usageTimeMinutes() != null) {
            bike.setUsageTimeMinutes(command.usageTimeMinutes());
        }

        if (command.isPublic() != null) {
            bike.setPublic(command.isPublic());
        }

        if (command.isExternalSync() != null) {
            bike.setExternalSync(command.isExternalSync());
        }
    }

    private void rejectBlank(String value, String messageKey) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException(messageKey);
        }
    }
}
