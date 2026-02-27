package com.quetoquenana.pedalpal.announcement.application.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/*
 * Use case for creating a new announcement.
 * It validates the input and handles exceptions.
 */

@RequiredArgsConstructor
@Slf4j
public class CreateAnnouncementUseCase {

    private final AnnouncementMapper mapper;
    private final AnnouncementRepository repository;

    @Transactional
    public AnnouncementResult execute(CreateAnnouncementCommand command) {
        validate(command);

        try {
            Announcement announcement = mapper.toModel(command);
            Announcement saved = repository.save(announcement);
            return mapper.toResult(saved);
        } catch (BadRequestException ex) {
            log.error("BadRequestException on CreateAnnouncementUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on CreateAnnouncementUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("announcement.creation.failed");
        }
    }

    private void validate(CreateAnnouncementCommand command) {
        rejectBlank(command.title(), "announcement.create.title.blank");

    }

    private void rejectBlank(String value, String messageKey) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(messageKey);
        }
    }
}
