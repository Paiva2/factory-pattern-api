package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.user.RegisterUserUsecase;
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

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegisterUserUsecaseImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDataProvider userDataProvider;

    @Mock
    private MailValidator mailValidator;

    private RegisterUserUsecase sut;

    private User userBuilder() {
        return User.builder()
            .id(1L)
            .email("any_email")
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .disabled(false)
            .disabledAt(Date.from(Instant.now()))
            .createdAt(Date.from(Instant.now()))
            .build();
    }

    @BeforeEach
    void setup() {
        this.sut = RegisterUserUsecaseImpl
            .builder()
            .passwordEncoder(this.passwordEncoder)
            .userDataProvider(this.userDataProvider)
            .mailValidator(this.mailValidator)
            .build();

        when(this.passwordEncoder.encode("any_password")).thenReturn("hashed_password");
        when(this.passwordEncoder.matches("any_password", "any_hashed_password")).thenReturn(true);
        when(this.mailValidator.validate("any_email")).thenReturn(true);

        when(userDataProvider.register(Mockito.any())).thenReturn(this.userBuilder());
    }

    @Test
    void shouldCallSutOnce() {
        User newUser = User.builder()
            .id(1L)
            .email("any_email")
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .disabled(false)
            .disabledAt(Date.from(Instant.now()))
            .build();

        RegisterUserUsecase sutMock = mock(RegisterUserUsecaseImpl.class);
        sutMock.exec(newUser);

        verify(sutMock, times(1)).exec(newUser);
        verify(sutMock, times(1)).exec(Mockito.argThat(arg -> arg.equals(newUser)));
    }

    @Test
    void shouldNotRegisterIfUserIsEmpty() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null);
        });

        Assertions.assertEquals(exception.getMessage(), "User can't be empty");
    }

    @Test
    void shouldNotRegisterIfEmailIsEmpty() {
        User user = this.userBuilder();
        user.setEmail(null);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "User e-mail can't be empty");
    }

    @Test
    void shouldNotRegisterIfNamesEmpty() {
        User user = this.userBuilder();
        user.setName(null);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "Name can't be empty");
    }

    @Test
    void shouldNotRegisterIfPasswordHasLessThanSixChars() {
        User user = this.userBuilder();
        user.setPassword("12345");

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "Password must have at least 6 characters");
    }

    @Test
    void shouldNotRegisterIfEmailHasInvalidFormat() {
        User user = this.userBuilder();
        user.setEmail("invalid_email");

        when(mailValidator.validate("invalid_email")).thenReturn(false);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(user);
        });

        Assertions.assertEquals(exception.getMessage(), "Invalid e-mail");
    }

    @Test
    void shouldNotRegisterIfUserAlreadyExists() {
        User newUser = User.builder()
            .id(1L)
            .email("any_email")
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .disabled(false)
            .disabledAt(Date.from(Instant.now()))
            .build();

        when(userDataProvider.findByEmail("any_email")).thenReturn(Optional.ofNullable(newUser));

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () -> {
            this.sut.exec(newUser);
        });

        Assertions.assertEquals(exception.getMessage(), "User already exists");
    }

    @Test
    void shouldHashPasswordCorrectly() {
        String hashedPassword = this.sut.encodePassword("any_password");

        verify(this.passwordEncoder, times(1)).encode("any_password");
        Assertions.assertEquals("hashed_password", hashedPassword);
    }

    @Test
    void shouldReturnUsecaseOutputIfNothingGoesWrong() {
        User user = this.userBuilder();

        UserOutputDTO sutOutput = this.sut.exec(user);

        Assertions.assertAll("Output Assertions",
            () -> Assertions.assertEquals(user.getEmail(), sutOutput.getEmail()),
            () -> Assertions.assertEquals(user.getId(), sutOutput.getId()),
            () -> Assertions.assertEquals(user.getRole(), sutOutput.getRole()),
            () -> Assertions.assertEquals(user.getName(), sutOutput.getName()),
            () -> Assertions.assertInstanceOf(Date.class, sutOutput.getCreatedAt())
        );
    }
}