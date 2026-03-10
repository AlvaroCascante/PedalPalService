package com.quetoquenana.pedalpal.announcement.application.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.port.UploadMediaPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * Use case for creating a new announcement.
 * It validates the input and handles exceptions.
 */

@RequiredArgsConstructor
public class CreateAnnouncementUseCase {

    private final AnnouncementMapper mapper;
    private final AnnouncementRepository repository;
    private final UploadMediaPort uploadMediaPort;

    @Transactional
    public AnnouncementResult execute(CreateAnnouncementCommand command) {
        validate(command);

        Announcement announcement = mapper.toModel(command);
        announcement = repository.save(announcement);

        UploadMediaCommand mediaRequest = mapper.toMediaUploadRequest(announcement, command);
        List<MediaResult> mediaResul =  uploadMediaPort.generateUploadUrls(mediaRequest);

        return mapper.toResult(announcement, mediaResul);
    }

    private void validate(CreateAnnouncementCommand command) {
        if (command.title() == null || command.title().trim().isEmpty()) {
            throw new BadRequestException("announcement.title.blank");
        }
    }
}
