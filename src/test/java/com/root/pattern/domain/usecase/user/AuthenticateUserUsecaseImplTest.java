package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.user.AuthenticateUserUsecase;
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
class AuthenticateUserUsecaseTest {
    @Mock
    private UserDataProvider userDataProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthenticateUserUsecase sut;

    @BeforeEach
    void setup() {
        this.sut = AuthenticateUserUsecaseImpl.builder()
            .userDataProvider(this.userDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .build();

        when(this.passwordEncoder.encode("any_password")).thenReturn("hashed_password");
        when(this.passwordEncoder.matches("any_password", "any_hashed_password")).thenReturn(true);

        User user = User.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .password("any_hashed_password")
            .role(Role.USER)
            .createdAt(Date.from(Instant.now()))
            .build();

        when(userDataProvider.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    void shouldNotAuthenticateUserWithoutEmailProvided() {
        User user = User.builder()
            .id(1L)
            .email(null)
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .disabled(false)
            .build();

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "User email can't be empty");
    }

    @Test
    void shouldNotAuthenticateUserWithoutPasswordProvided() {
        User user = User.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.USER)
            .password(null)
            .disabled(false)
            .build();

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "User password can't be empty");
    }

    @Test
    void shouldNotAuthenticateUserIfUserDontExists() {
        User user = User.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .disabled(false)
            .build();

        when(userDataProvider.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "User not found");
    }

    @Test
    void shouldNotAuthenticateUserIfUserIsDisabled() {
        User user = User.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .disabled(true)
            .disabledAt(Date.from(Instant.now()))
            .build();

        Exception exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "User is disabled");
    }

    @Test
    void shouldNotAuthenticateUserIfCredentialsAreWrong() {
        User user = User.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .disabled(true)
            .build();

        when(passwordEncoder.matches("any_password", "any_hashed_password")).thenReturn(false);

        Exception exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "Wrong credentials");
    }

    @Test
    void shouldAuthenticateUserCorrectlyIfNothingGoesWrong() {
        User user = User.builder()
            .id(1L)
            .email("any_email@email.com")
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .disabled(true)
            .build();

        UserOutputDTO sutOutput = this.sut.exec(user);

        Assertions.assertAll("Output Assertions",
            () -> Assertions.assertEquals(sutOutput.getId(), user.getId()),
            () -> Assertions.assertEquals(sutOutput.getEmail(), user.getEmail()),
            () -> Assertions.assertEquals(sutOutput.getName(), user.getName()),
            () -> Assertions.assertEquals(sutOutput.getRole(), user.getRole()),
            () -> Assertions.assertNotNull(sutOutput.getCreatedAt())
        );
    }
}
