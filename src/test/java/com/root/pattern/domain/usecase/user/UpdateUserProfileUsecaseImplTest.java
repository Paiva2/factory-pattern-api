package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.UpdateUserProfileUsecase;
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
class UpdateUserProfileUsecaseImplTest {
    @Mock
    private UserDataProvider userDataProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailValidator mailValidator;

    @Mock
    private PropertiesCopier propertiesCopier;

    private UpdateUserProfileUsecase sut;

    private User userBuilder(Long id) {
        return User.builder()
            .id(id)
            .email("any_email")
            .name("any_name")
            .role(Role.USER)
            .password("any_password")
            .createdAt(new Date())
            .updatedAt(new Date())
            .disabledAt(null)
            .build();
    }

    @BeforeEach
    void setup() {
        this.sut = UpdateUserProfileUsecaseImpl.builder()
            .userDataProvider(this.userDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .mailValidator(this.mailValidator)
            .propertiesCopier(this.propertiesCopier)
            .build();

        User user = this.userBuilder(1L);

        Mockito.when(this.mailValidator.validate(Mockito.anyString())).thenReturn(true);
        Mockito.when(this.userDataProvider.findByEmail(Mockito.anyString())).thenReturn(
            Optional.ofNullable(user)
        );
        Mockito.when(this.userDataProvider.findById(Mockito.anyLong())).thenReturn(
            Optional.ofNullable(user)
        );
        Mockito.when(this.passwordEncoder.encode(Mockito.anyString())).thenReturn("hashed_password");
        Mockito.when(this.userDataProvider.update(Mockito.any())).thenReturn(user);
    }

    @Test
    void shouldCallSutWithCorrectlyProvidedParams() {
        User input = this.userBuilder(1L);
        input.setName("new_valid_name");
        input.setEmail("new_valid_email");
        input.setPassword("new_valid_password");

        UpdateUserProfileUsecaseImpl mockSut = Mockito.mock(UpdateUserProfileUsecaseImpl.class);
        mockSut.exec(input);

        Mockito.verify(mockSut, Mockito.times(1)).exec(input);
    }

    @Test
    void shouldThrowErrorIfNewEmailIsInvalid() {
        User input = this.userBuilder(1L);
        input.setEmail("invalid_email");

        Mockito.when(this.mailValidator.validate("invalid_email")).thenReturn(false);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("Invalid new e-mail", exception.getMessage());
    }

    @Test
    void shouldThrowErrorIfNewPasswordHasLessThanSixChars() {
        User input = this.userBuilder(1L);
        input.setPassword("12345");

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("New password must have at least 6 characters", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfUserNotExists() {
        User input = this.userBuilder(1L);

        Mockito.when(this.userDataProvider.findById(input.getId())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfUserIsDisabled() {
        User input = this.userBuilder(1L);
        input.setDisabled(true);
        input.setDisabledAt(new Date());

        Mockito.when(this.userDataProvider.findById(input.getId())).thenReturn(Optional.of(input));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("User disabled", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfNewEmailAlreadyExists() {
        User input = this.userBuilder(1L);
        input.setEmail("existent_new_email");

        User existentUserWithEmail = this.userBuilder(35L);
        input.setEmail("existent_email");

        Mockito.when(this.userDataProvider.findByEmail(Mockito.anyString())).thenReturn(Optional.of(existentUserWithEmail));

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () -> {
            this.sut.exec(input);
        });

        Assertions.assertEquals("E-mail already being used", exception.getMessage());
    }

    @Test
    void shouldDoNothingWhenProvidedEmailIsAlreadyMine() {
        this.sut.exec(this.userBuilder(1L));
    }

    @Test
    void shouldHashNewPasswordCorrectly() {
        String hashedPass = this.sut.hashNewPassword("raw_password");

        Assertions.assertEquals("hashed_password", hashedPass);
    }

    @Test
    void shouldMountOutputWithUpdatedInfosIfNothingGoesWrong() {
        User input = this.userBuilder(1L);
        input.setEmail("new_valid_email");
        input.setName("new_valid_name");
        input.setPassword("new_valid_password");

        Mockito.when(this.userDataProvider.update(Mockito.any())).thenReturn(input);

        UserOutputDTO outputSut = this.sut.exec(input);

        Assertions.assertAll("sut output assertions",
            () -> Assertions.assertEquals(input.getId(), outputSut.getId()),
            () -> Assertions.assertEquals(input.getName(), outputSut.getName()),
            () -> Assertions.assertEquals(input.getEmail(), outputSut.getEmail()),
            () -> Assertions.assertEquals(input.getRole(), outputSut.getRole()),
            () -> Assertions.assertEquals(input.getCreatedAt(), outputSut.getCreatedAt())
        );
    }
}