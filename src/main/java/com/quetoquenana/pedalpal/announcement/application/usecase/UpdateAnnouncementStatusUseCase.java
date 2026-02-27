package com.quetoquenana.pedalpal.announcement.application.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementStatusCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/*
 * Use case for updating the status of an announcement.
 * It retrieves the announcement, updates its status based on the command, and saves it.
 * It also handles exceptions and logs errors.
 */

@RequiredArgsConstructor
@Slf4j
public class UpdateAnnouncementStatusUseCase {

    private final AnnouncementRepository repository;
    private final AnnouncementMapper mapper;

    @Transactional
    public AnnouncementResult execute(UpdateAnnouncementStatusCommand command) {
        Announcement announcement = repository.getById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("announcement.not.found"));

        try {
            if (command.status() == null || command.status().trim().isEmpty()) {
                throw new BadRequestException("announcement.update.status.required");
            }

            announcement.setStatus(GeneralStatus.from(command.status()));
            Announcement saved = repository.save(announcement);
            return mapper.toResult(saved);
        } catch (BadRequestException ex) {
            log.error("BadRequestException on UpdateAnnouncementStatusUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateAnnouncementStatusUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("announcement.update.status.failed");
        }
    }
}
