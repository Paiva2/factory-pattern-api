package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.musician.UpdateMusicianProfileUsecase;
import com.root.pattern.domain.strategy.context.MailValidator;
import com.root.pattern.domain.strategy.context.PropertiesCopier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UpdateMusicianProfileUsecaseImplTest {
    @Mock
    private MusicianDataProvider musicianDataProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailValidator mailValidator;

    @Mock
    private PropertiesCopier propertiesCopier;

    private UpdateMusicianProfileUsecase sut;

    private Musician musicianBuilder(Long id) {
        return Musician.builder()
            .id(id)
            .email("any_email")
            .name("any_name")
            .role(Role.MUSICIAN)
            .password("any_password")
            .createdAt(new Date())
            .updatedAt(new Date())
            .disabledAt(null)
            .build();
    }

    @BeforeEach
    void setup() {
        this.sut = UpdateMusicianProfileUsecaseImpl.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .mailValidator(this.mailValidator)
            .propertiesCopier(this.propertiesCopier)
            .build();

        Musician musician = this.musicianBuilder(1L);

        Mockito.when(this.mailValidator.validate(Mockito.anyString())).thenReturn(true);
        Mockito.when(this.musicianDataProvider.findByEmail(Mockito.anyString())).thenReturn(
            Optional.ofNullable(musician)
        );
        Mockito.when(this.musicianDataProvider.findById(Mockito.anyLong())).thenReturn(
            Optional.ofNullable(musician)
        );
        Mockito.when(this.passwordEncoder.encode(Mockito.anyString())).thenReturn("hashed_password");
        Mockito.when(this.musicianDataProvider.update(Mockito.any())).thenReturn(musician);
    }

    @Test
    void shouldCallSutWithCorrectlyProvidedParams() {
        Musician input = this.musicianBuilder(1L);
        input.setName("new_valid_name");
        input.setEmail("new_valid_email");
        input.setPassword("new_valid_password");

        UpdateMusicianProfileUsecaseImpl mockSut = Mockito.mock(UpdateMusicianProfileUsecaseImpl.class);
        mockSut.exec(input);

        Mockito.verify(mockSut, Mockito.times(1)).exec(input);
    }

    @Test
    void shouldThrowErrorIfNewEmailIsInvalid() {
        Musician input = this.musicianBuilder(1L);
        input.setEmail("invalid_email");

        Mockito.when(this.mailValidator.validate("invalid_email")).thenReturn(false);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("Invalid e-mail", exception.getMessage());
    }

    @Test
    void shouldThrowErrorIfNewPasswordHasLessThanSixChars() {
        Musician input = this.musicianBuilder(1L);
        input.setPassword("12345");

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("Password must have at least 6 characters", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfMusicianNotExists() {
        Musician input = this.musicianBuilder(1L);

        Mockito.when(this.musicianDataProvider.findById(input.getId())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("Musician not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfMusicianIsDisabled() {
        Musician input = this.musicianBuilder(1L);
        input.setDisabled(true);
        input.setDisabledAt(new Date());

        Mockito.when(this.musicianDataProvider.findById(input.getId())).thenReturn(Optional.of(input));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("Musician disabled", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfNewEmailAlreadyExists() {
        Musician input = this.musicianBuilder(1L);
        input.setEmail("existent_new_email");

        Musician existentMusicianWithEmail = this.musicianBuilder(35L);
        input.setEmail("existent_email");

        Mockito.when(this.musicianDataProvider.findByEmail(Mockito.anyString())).thenReturn(
            Optional.of(existentMusicianWithEmail)
        );

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("E-mail already exists", exception.getMessage());
    }

    @Test
    void shouldDoNothingWhenProvidedEmailIsAlreadyMine() {
        this.sut.exec(this.musicianBuilder(1L));
    }

    @Test
    void shouldHashNewPasswordCorrectly() {
        String hashedPass = this.sut.hashNewPassword("raw_password");

        Assertions.assertEquals("hashed_password", hashedPass);
    }

    @Test
    void shouldMountOutputWithUpdatedInfosIfNothingGoesWrong() {
        Musician input = this.musicianBuilder(1L);
        input.setEmail("new_valid_email");
        input.setName("new_valid_name");
        input.setPassword("new_valid_password");

        Mockito.when(this.musicianDataProvider.update(Mockito.any())).thenReturn(input);

        MusicianOutputDTO outputSut = this.sut.exec(input);

        Assertions.assertAll("sut output assertions",
            () -> Assertions.assertEquals(input.getId(), outputSut.getId()),
            () -> Assertions.assertEquals(input.getName(), outputSut.getName()),
            () -> Assertions.assertEquals(input.getEmail(), outputSut.getEmail()),
            () -> Assertions.assertEquals(input.getRole(), outputSut.getRole()),
            () -> Assertions.assertEquals(input.getCreatedAt(), outputSut.getCreatedAt())
        );
    }

}