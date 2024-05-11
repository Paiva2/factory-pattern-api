package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticateMusicianUsecaseTest {
    @Mock
    private MusicianDataProvider musicianDataProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthMusicianUsecase sut;

    @BeforeEach
    void setup() {
        this.sut = AuthMusicianUsecase.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .build();

        when(this.passwordEncoder.encode("any_password")).thenReturn("hashed_password");
        when(this.passwordEncoder.matches("any_password", "any_hashed_password")).thenReturn(true);

        Musician musician = Musician.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .password("any_hashed_password")
            .role(Role.MUSICIAN)
            .createdAt(Date.from(Instant.now()))
            .build();

        when(musicianDataProvider.findByEmail(musician.getEmail())).thenReturn(Optional.of(musician));
    }

    @Test
    void shouldNotAuthenticateMusicianWithoutEmailProvided() {
        Musician musician = Musician.builder()
            .id(1L)
            .email(null)
            .name("any_name")
            .role(Role.MUSICIAN)
            .password("any_password")
            .disabled(false)
            .build();

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(musician);
        });

        Assertions.assertEquals(exception.getMessage(), "Musician e-mail can't be empty");
    }

    @Test
    void shouldNotAuthenticateMusicianWithoutPasswordProvided() {
        Musician musician = Musician.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.MUSICIAN)
            .password(null)
            .disabled(false)
            .build();

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(musician);
        });

        Assertions.assertEquals(exception.getMessage(), "Musician password can't be empty");
    }

    @Test
    void shouldNotAuthenticateMusicianIfMusicianDontExists() {
        Musician musician = Musician.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.MUSICIAN)
            .password("any_password")
            .disabled(false)
            .build();

        when(musicianDataProvider.findByEmail(musician.getEmail())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(musician);
        });

        Assertions.assertEquals(exception.getMessage(), "Musician not found");
    }

    @Test
    void shouldNotAuthenticateMusicianIfCredentialsAreWrong() {
        Musician musician = Musician.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.MUSICIAN)
            .password("any_password")
            .disabled(true)
            .build();

        when(passwordEncoder.matches("any_password", "any_hashed_password")).thenReturn(false);

        Exception exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(musician);
        });

        Assertions.assertEquals(exception.getMessage(), "Wrong credentials");
    }

    @Test
    void shouldAuthenticateMusicianCorrectlyIfNothingGoesWrong() {
        Musician musician = Musician.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.MUSICIAN)
            .password("any_password")
            .disabled(true)
            .build();

        MusicianOutputDTO sutOutput = this.sut.exec(musician);

        Assertions.assertAll("Output Assertions",
            () -> Assertions.assertEquals(sutOutput.getId(), musician.getId()),
            () -> Assertions.assertEquals(sutOutput.getEmail(), musician.getEmail()),
            () -> Assertions.assertEquals(sutOutput.getName(), musician.getName()),
            () -> Assertions.assertEquals(sutOutput.getRole(), musician.getRole()),
            () -> Assertions.assertNotNull(sutOutput.getCreatedAt())
        );
    }
}
