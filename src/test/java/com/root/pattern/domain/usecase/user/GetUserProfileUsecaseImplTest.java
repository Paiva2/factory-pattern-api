package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
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
class GetUserProfileUsecaseImplTest {
    @Mock
    private UserDataProvider userDataProvider;

    private GetUserProfileUsecaseImpl sut;

    private User userBuilder() {
        return User.builder()
            .id(1L)
            .name("any_name")
            .email("any_email")
            .password("any_password")
            .createdAt(new Date())
            .role(Role.USER)
            .build();
    }

    @BeforeEach
    void setup() {
        this.sut = GetUserProfileUsecaseImpl
            .builder()
            .userDataProvider(this.userDataProvider)
            .build();

        Mockito.when(this.userDataProvider.findById(Mockito.any()))
            .thenReturn(Optional.ofNullable(this.userBuilder()));
    }

    @Test
    void shouldCallSutWithCorrectProvidedParams() {
        GetUserProfileUsecaseImpl mockSut = Mockito.mock(GetUserProfileUsecaseImpl.class);
        mockSut.exec(1L);

        Mockito.verify(mockSut, Mockito.times(1)).exec(1L);
    }

    @Test
    void shouldThrowErrorIfIdIsNotProvided() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null);
        });

        Assertions.assertEquals("User id can't be empty", exception.getMessage());
    }

    @Test
    void shouldThrowErrorIfUserDontExists() {
        Mockito.when(this.userDataProvider.findById(Mockito.any()))
            .thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L);
        });

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldReturnUserProfileIfNothingGoesWrong() {
        UserOutputDTO sutOutput = this.sut.exec(this.userBuilder().getId());

        Assertions.assertAll("Sut return",
            () -> Assertions.assertEquals(this.userBuilder().getId(), sutOutput.getId()),
            () -> Assertions.assertEquals(this.userBuilder().getName(), sutOutput.getName()),
            () -> Assertions.assertEquals(this.userBuilder().getEmail(), sutOutput.getEmail()),
            () -> Assertions.assertNotNull(sutOutput.getCreatedAt()),
            () -> Assertions.assertEquals(this.userBuilder().getRole(), sutOutput.getRole())
        );
    }

}