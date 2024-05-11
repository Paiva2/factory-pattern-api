package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.musician.RegisterMusicianUsecase;
import com.root.pattern.domain.strategy.context.MailValidator;
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
class RegisterMusicianUsecaseImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MusicianDataProvider musicianDataProvider;

    @Mock
    private MailValidator mailValidator;

    private RegisterMusicianUsecase sut;

    private Musician musicianBuilder() {
        return Musician.builder()
            .email("any_email")
            .name("any_name")
            .password("any_password")
            .build();
    }

    @BeforeEach
    public void setup() {
        this.sut = RegisterMusicianUsecaseImpl
            .builder()
            .musicianDataProvider(this.musicianDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .mailValidator(this.mailValidator)
            .build();

        Musician createdMusicianMock = this.musicianBuilder();

        Mockito.when(this.musicianDataProvider.findByEmailOrName(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(this.passwordEncoder.encode(Mockito.any())).thenReturn("hashed_password");
        Mockito.when(this.mailValidator.validate(Mockito.anyString())).thenReturn(true);

        createdMusicianMock.setId(1L);
        createdMusicianMock.setCreatedAt(new Date());
        createdMusicianMock.setRole(Role.MUSICIAN);
        createdMusicianMock.setUpdatedAt(new Date());

        Mockito.when(this.musicianDataProvider.register(Mockito.any())).thenReturn(createdMusicianMock);
    }

    @Test
    void shouldCallSutWithCorrectProvidedParams() {
        RegisterMusicianUsecase mockSut = Mockito.mock(RegisterMusicianUsecaseImpl.class);
        Musician input = this.musicianBuilder();

        mockSut.exec(input);

        Mockito.verify(mockSut, Mockito.times(1)).exec(input);
    }

    @Test
    void shouldThrowErrorWhenNewMusicianIsNotProvided() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null);
        });

        Assertions.assertEquals("Musician can't be null", exception.getMessage());
    }

    @Test
    void shouldThrowErrorWhenEmailIsNotProvided() {
        Musician newMusician = this.musicianBuilder();
        newMusician.setEmail(null);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(newMusician);
        });

        Assertions.assertEquals("Musician e-mail can't be empty", exception.getMessage());
    }

    @Test
    void shouldThrowErrorWhenNameIsNotProvided() {
        Musician newMusician = this.musicianBuilder();
        newMusician.setName(null);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(newMusician);
        });

        Assertions.assertEquals("Musician name can't be empty", exception.getMessage());
    }

    @Test
    void shouldThrowErrorWhenPasswordHasLessThan6Chars() {
        Musician newMusician = this.musicianBuilder();
        newMusician.setPassword("12345");

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(newMusician);
        });

        Assertions.assertEquals("Password must have at least 6 characters", exception.getMessage());
    }

    @Test
    void shouldThrowErrorEmailProvidedIsNotValid() {
        Musician newMusician = this.musicianBuilder();

        Mockito.when(this.mailValidator.validate(Mockito.anyString())).thenReturn(false);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(newMusician);
        });

        Assertions.assertEquals("Invalid e-mail", exception.getMessage());
    }

    @Test
    void shouldThrowErrorWhenMusicianAlreadyExists() {
        Musician newMusician = this.musicianBuilder();

        Mockito.when(this.musicianDataProvider.findByEmailOrName(Mockito.any(), Mockito.any())).thenReturn(
            Optional.ofNullable(this.musicianBuilder())
        );

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () -> {
            this.sut.exec(newMusician);
        });

        Assertions.assertEquals("Musician already exists", exception.getMessage());
    }

    @Test
    void shouldCorrectlyHashPassword() {
        Musician newMusician = this.musicianBuilder();

        String hashedPassword = this.sut.hashPassword(newMusician.getPassword());

        Assertions.assertEquals("hashed_password", hashedPassword);
    }

    @Test
    void shouldReturnSutOutputIfNothingGoesWrong() {
        Musician newMusician = this.musicianBuilder();

        MusicianOutputDTO outputSut = this.sut.exec(newMusician);

        Assertions.assertAll("Sut output assertions",
            () -> Assertions.assertNotNull(outputSut.getId()),
            () -> Assertions.assertNotNull(outputSut.getCreatedAt()),
            () -> Assertions.assertEquals(Role.MUSICIAN, outputSut.getRole()),
            () -> Assertions.assertEquals(newMusician.getName(), outputSut.getName()),
            () -> Assertions.assertEquals(newMusician.getEmail(), outputSut.getEmail())
        );
    }
}