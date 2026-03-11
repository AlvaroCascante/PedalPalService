package com.quetoquenana.pedalpal.announcement.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.model.AnnouncementStatus;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.application.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.port.UploadMediaPort;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.util.TestAnnouncementData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAnnouncementUseCaseTest {

    @Mock
    AnnouncementRepository repository;

    @Mock
    AnnouncementMapper mapper;

    @Mock
    UploadMediaPort uploadMediaPort;

    @InjectMocks
    CreateAnnouncementUseCase useCase;

    @Captor
    ArgumentCaptor<Announcement> announcementCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldCreateAnnouncement_whenCommandIsValid() {
            UUID savedId = UUID.randomUUID();

            CreateAnnouncementCommand command = TestAnnouncementData.createCommand();

            Announcement model = Announcement.builder()
                    .title(command.title())
                    .subTitle(command.subTitle())
                    .description(command.description())
                    .position(command.position())
                    .url(command.url())
                    .status(AnnouncementStatus.DRAFT)
                    .build();

            Announcement saved = TestAnnouncementData.existingAnnouncement(savedId);

            UploadMediaCommand mediaCommand = new UploadMediaCommand(
                    true,
                    savedId,
                    MediaReferenceType.ANNOUNCEMENT,
                    java.util.Set.of()
            );
            java.util.List<MediaResult> mediaResult = java.util.List.of(
                    new MediaResult(
                            UUID.randomUUID(),
                            "image/jpeg",
                            "r2",
                            true,
                            com.quetoquenana.pedalpal.media.domain.model.MediaStatus.DRAFT,
                            "front",
                            "Front view",
                            "https://upload.url",
                            Instant.now().plusSeconds(60)
                    )
            );

            AnnouncementResult result = TestAnnouncementData.result(savedId);

            when(mapper.toModel(command)).thenReturn(model);
            when(repository.save(any(Announcement.class))).thenReturn(saved);
            when(mapper.toMediaUploadRequest(saved, command)).thenReturn(mediaCommand);
            when(uploadMediaPort.generateUploadUrls(mediaCommand)).thenReturn(mediaResult);
            when(mapper.toResult(saved, mediaResult)).thenReturn(result);

            AnnouncementResult actual = useCase.execute(command);

            assertNotNull(actual);
            assertEquals(savedId, actual.id());

            verify(repository, times(1)).save(announcementCaptor.capture());
            Announcement toSave = announcementCaptor.getValue();
            assertEquals("Title", toSave.getTitle());
        }

        @Test
        void shouldReturnMediaResults_whenMediaUrlsGenerated() {
            UUID savedId = UUID.randomUUID();

            CreateAnnouncementCommand command = TestAnnouncementData.createCommand();

            Announcement model = Announcement.builder()
                    .title(command.title())
                    .status(AnnouncementStatus.DRAFT)
                    .build();

            Announcement saved = TestAnnouncementData.existingAnnouncement(savedId);

            UploadMediaCommand mediaCommand = new UploadMediaCommand(
                    true,
                    savedId,
                    MediaReferenceType.ANNOUNCEMENT,
                    java.util.Set.of()
            );

            MediaResult m1 = new MediaResult(
                    UUID.randomUUID(),
                    "image/jpeg",
                    "r2",
                    true,
                    com.quetoquenana.pedalpal.media.domain.model.MediaStatus.DRAFT,
                    "front",
                    "Front view",
                    "https://upload.url/1",
                    Instant.now().plusSeconds(60)
            );
            MediaResult m2 = new MediaResult(
                    UUID.randomUUID(),
                    "image/png",
                    "r2",
                    false,
                    com.quetoquenana.pedalpal.media.domain.model.MediaStatus.DRAFT,
                    "side",
                    "Side view",
                    "https://upload.url/2",
                    Instant.now().plusSeconds(60)
            );
            java.util.List<MediaResult> mediaResults = java.util.List.of(m1, m2);

            AnnouncementResult expected = new AnnouncementResult(
                    savedId,
                    saved.getTitle(),
                    saved.getSubTitle(),
                    saved.getDescription(),
                    saved.getPosition(),
                    saved.getUrl(),
                    saved.getStatus(),
                    mediaResults
            );

            when(mapper.toModel(command)).thenReturn(model);
            when(repository.save(any(Announcement.class))).thenReturn(saved);
            when(mapper.toMediaUploadRequest(saved, command)).thenReturn(mediaCommand);
            when(uploadMediaPort.generateUploadUrls(mediaCommand)).thenReturn(mediaResults);
            when(mapper.toResult(saved, mediaResults)).thenReturn(expected);

            AnnouncementResult actual = useCase.execute(command);

            assertNotNull(actual);
            assertNotNull(actual.mediaResults());
            assertEquals(2, actual.mediaResults().size());
            assertTrue(actual.mediaResults().contains(m1));
            assertTrue(actual.mediaResults().contains(m2));

            verify(uploadMediaPort, times(1)).generateUploadUrls(mediaCommand);
            verify(mapper, times(1)).toResult(saved, mediaResults);
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldThrowBadRequest_whenTitleIsBlank() {
            CreateAnnouncementCommand command = new CreateAnnouncementCommand(
                    "   ",
                    null,
                    null,
                    null,
                    null,
                    java.util.List.of()
            );

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("announcement.title.blank", ex.getMessage());
        }
    }
}
