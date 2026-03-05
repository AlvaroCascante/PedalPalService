package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.application.port.CdnUrlProvider;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmMediaUploadUseCaseTest {

    @Mock
    MediaRepository repository;

    @Mock
    MediaMapper mapper;

    @Mock
    CdnUrlProvider cdnUrlProvider;

    ConfirmMediaUploadUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConfirmMediaUploadUseCase(repository, mapper, cdnUrlProvider);
    }

    @Test
    void shouldThrowWhenMediaNotFound() {
        UUID mediaId = UUID.randomUUID();
        ConfirmUploadCommand command = new ConfirmUploadCommand(mediaId, UUID.randomUUID());
        when(repository.getById(mediaId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));

        verify(repository, never()).save(any(Media.class));
    }

    @Test
    void shouldThrowWhenMediaNotDraft() {
        UUID mediaId = UUID.randomUUID();
        ConfirmUploadCommand command = new ConfirmUploadCommand(mediaId, UUID.randomUUID());
        Media model = Media.builder()
                .id(mediaId)
                .storageKey("key")
                .status(MediaStatus.ACTIVE)
                .build();

        when(repository.getById(mediaId)).thenReturn(Optional.of(model));

        assertThrows(BusinessException.class, () -> useCase.execute(command));

        verify(repository, never()).save(any(Media.class));
    }

    @Test
    void shouldConfirmUploadAndReturnResult() {
        UUID mediaId = UUID.randomUUID();
        ConfirmUploadCommand command = new ConfirmUploadCommand(mediaId, UUID.randomUUID());
        Media model = Media.builder()
                .id(mediaId)
                .storageKey("key")
                .status(MediaStatus.DRAFT)
                .build();

        ConfirmedUploadResult result = new ConfirmedUploadResult(mediaId, "key", "ACTIVE", "https://cdn");

        when(repository.getById(mediaId)).thenReturn(Optional.of(model));
        when(repository.save(model)).thenReturn(model);
        when(cdnUrlProvider.providePublic("key")).thenReturn("https://cdn");
        when(mapper.toConfirmedResult(model, "https://cdn")).thenReturn(result);

        ConfirmedUploadResult actual = useCase.execute(command);

        assertEquals(result, actual);
        verify(repository).save(model);
    }
}
