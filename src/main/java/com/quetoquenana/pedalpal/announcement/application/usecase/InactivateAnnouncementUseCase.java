package com.quetoquenana.pedalpal.announcement.application.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/*
 * Use case for updating the status of an announcement.
 * It retrieves the announcement, updates its status based on the command, and saves it.
 * It also handles exceptions and logs errors.
 */
@RequiredArgsConstructor
public class InactivateAnnouncementUseCase {

    private final AnnouncementRepository repository;
    private final AnnouncementMapper mapper;

    @Transactional
    public AnnouncementResult execute(UpdateAnnouncementCommand command) {
        Announcement announcement = repository.getById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("announcement.not.found"));

        announcement.inactivate();
        repository.save(announcement);
        return mapper.toResult(announcement);
    }
}
