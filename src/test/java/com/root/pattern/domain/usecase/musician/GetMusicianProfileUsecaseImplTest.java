package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.musician.GetMusicianProfileUsecase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GetMusicianProfileUsecaseImplTest {
    @Mock
    private MusicianDataProvider musicianDataProvider;

    private GetMusicianProfileUsecase sut;

    private Musician musicianBuilder() {
        return Musician.builder()
            .id(1L)
            .name("any_name")
            .email("any_email")
            .password("any_password")
            .createdAt(new Date())
            .role(Role.MUSICIAN)
            .build();
    }

    @BeforeEach
    void setup() {
        this.sut = GetMusicianProfileUsecaseImpl
            .builder()
            .musicianDataProvider(this.musicianDataProvider)
            .build();

        Mockito.when(this.musicianDataProvider.findById(Mockito.any()))
            .thenReturn(Optional.ofNullable(this.musicianBuilder()));
    }

    @Test
    void shouldCallSutWithCorrectProvidedParams() {
        GetMusicianProfileUsecaseImpl mockSut = Mockito.mock(GetMusicianProfileUsecaseImpl.class);
        mockSut.exec(1L);

        Mockito.verify(mockSut, Mockito.times(1)).exec(1L);
    }

    @Test
    void shouldThrowErrorIfIdIsNotProvided() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null);
        });

        Assertions.assertEquals("Musician id can't be empty", exception.getMessage());
    }

    @Test
    void shouldThrowErrorIfMusicianDontExists() {
        Mockito.when(this.musicianDataProvider.findById(Mockito.any()))
            .thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L);
        });

        Assertions.assertEquals("Musician not found", exception.getMessage());
    }

    @Test
    void shouldThrowErrorIfMusicianIsDisabled() {
        Musician musician = this.musicianBuilder();
        musician.setDisabled(true);

        Mockito.when(this.musicianDataProvider.findById(Mockito.any()))
            .thenReturn(Optional.of(musician));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(1L);
        });

        Assertions.assertEquals("Musician is disabled", exception.getMessage());
    }

    @Test
    void shouldReturnMusicianProfileIfNothingGoesWrong() {
        MusicianOutputDTO sutOutput = this.sut.exec(this.musicianBuilder().getId());

        Assertions.assertAll("Sut return",
            () -> Assertions.assertEquals(this.musicianBuilder().getId(), sutOutput.getId()),
            () -> Assertions.assertEquals(this.musicianBuilder().getName(), sutOutput.getName()),
            () -> Assertions.assertEquals(this.musicianBuilder().getEmail(), sutOutput.getEmail()),
            () -> Assertions.assertNotNull(sutOutput.getCreatedAt()),
            () -> Assertions.assertEquals(this.musicianBuilder().getRole(), sutOutput.getRole())
        );
    }

}