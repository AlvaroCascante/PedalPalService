package com.quetoquenana.pedalpal.announcement.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    CreateAnnouncementUseCase useCase;

    @Captor
    ArgumentCaptor<Announcement> announcementCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldCreateAnnouncement_whenCommandIsValid() {
            UUID authUserId = UUID.randomUUID();
            UUID savedId = UUID.randomUUID();

            CreateAnnouncementCommand command = TestAnnouncementData.createCommand(authUserId);
            Announcement model = Announcement.builder()
                    .title(command.title())
                    .subTitle(command.subTitle())
                    .description(command.description())
                    .position(command.position())
                    .url(command.url())
                    .status(GeneralStatus.ACTIVE)
                    .build();

            Announcement saved = TestAnnouncementData.existingAnnouncement(savedId);
            AnnouncementResult result = TestAnnouncementData.result(savedId);

            when(mapper.toModel(command)).thenReturn(model);
            when(repository.save(any(Announcement.class))).thenReturn(saved);
            when(mapper.toResult(saved)).thenReturn(result);

            AnnouncementResult actual = useCase.execute(command);

            assertNotNull(actual);
            assertEquals(savedId, actual.id());

            verify(repository, times(1)).save(announcementCaptor.capture());
            Announcement toSave = announcementCaptor.getValue();
            assertEquals("Title", toSave.getTitle());
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldThrowBadRequest_whenTitleIsBlank() {
            CreateAnnouncementCommand command = new CreateAnnouncementCommand(
                    UUID.randomUUID(),
                    "   ",
                    null,
                    null,
                    null,
                    null
            );

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("announcement.create.title.blank", ex.getMessage());
        }
    }
}
