package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.AuthenticateUserUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Objects;

@Builder
@AllArgsConstructor
public class AuthenticateUserUsecaseImpl implements AuthenticateUserUsecase {
    private final UserDataProvider userDataProvider;
    private final PasswordEncoder passwordEncoder;

    public UserOutputDTO exec(User user) {
        this.inputValidations(user);

        User getUser = this.validateIfUserExists(user.getEmail());

        this.validateIfUserIsDisabled(user.getDisabledAt());
        this.checkCredentials(user.getPassword(), getUser.getPassword());

        return this.mountOutputDto(getUser);
    }

    public void inputValidations(User user) {
        if (Objects.isNull(user.getEmail())) {
            throw new BadRequestException("User email can't be empty");
        }

        if (Objects.isNull(user.getPassword())) {
            throw new BadRequestException("User password can't be empty");
        }
    }

    public User validateIfUserExists(String userEmail) {
        User getUser = this.userDataProvider.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User"));

        return getUser;
    }

    public void validateIfUserIsDisabled(Date disabledAt) {
        if (Objects.nonNull(disabledAt)) {
            throw new ForbiddenException("User is disabled");
        }
    }

    public void checkCredentials(String rawPass, String hashedPass) {
        if (!this.passwordEncoder.matches(rawPass, hashedPass)) {
            throw new ForbiddenException("Wrong credentials");
        }
    }

    public UserOutputDTO mountOutputDto(User user) {
        return UserOutputDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
