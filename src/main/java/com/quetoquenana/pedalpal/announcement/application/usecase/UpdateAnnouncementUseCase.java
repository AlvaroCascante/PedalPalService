package com.quetoquenana.pedalpal.announcement.application.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/*
 * Use case for updating an announcement.
 * It retrieves the announcement, applies updates based on the command, and saves it.
 * It also handles exceptions and logs errors.
 */

@RequiredArgsConstructor
@Slf4j
public class UpdateAnnouncementUseCase {

    private final AnnouncementRepository repository;
    private final AnnouncementMapper mapper;

    @Transactional
    public AnnouncementResult execute(UpdateAnnouncementCommand command) {
        Announcement announcement = repository.getById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("announcement.not.found"));

        try {
            validate(command);
            mapper.applyPatch(announcement, command);
            Announcement saved = repository.save(announcement);
            return mapper.toResult(saved);
        } catch (BadRequestException ex) {
            log.error("BadRequestException on UpdateAnnouncementUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateAnnouncementUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("announcement.update.failed");
        }
    }

    private void validate(UpdateAnnouncementCommand command) {
        rejectBlankIfPresent(command.title(), "announcement.update.title.blank");
        rejectBlankIfPresent(command.subTitle(), "announcement.update.subtitle.blank");
        rejectBlankIfPresent(command.url(), "announcement.update.url.blank");
    }

    private void rejectBlankIfPresent(String value, String messageKey) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException(messageKey);
        }
    }
}
