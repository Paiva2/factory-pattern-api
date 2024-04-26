package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.RegisterUserUsecase;
import com.root.pattern.domain.strategy.context.MailValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

@Builder
@AllArgsConstructor
public class RegisterUserUsecaseImpl implements RegisterUserUsecase {
    private final UserDataProvider userDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailValidator mailValidator;

    public UserOutputDTO exec(User newUser) {
        this.inputValidations(newUser);
        this.validateIfUserAlreadyExists(newUser);

        String hashPassword = this.encodePassword(newUser.getPassword());

        newUser.setPassword(hashPassword);
        newUser.setRole(Role.USER);

        User register = this.userDataProvider.register(newUser);

        return this.mountOutputDto(register);
    }

    public void inputValidations(User dto) {
        if (Objects.isNull(dto)) {
            throw new BadRequestException("User can't be empty");
        }

        if (Objects.isNull(dto.getEmail())) {
            throw new BadRequestException("User e-mail can't be empty");
        }

        if (Objects.isNull(dto.getName())) {
            throw new BadRequestException("Name can't be empty");
        }

        if (dto.getPassword().length() < 6) {
            throw new BadRequestException("Password must have at least 6 characters");
        }

        if (!this.mailValidator.validate(dto.getEmail())) {
            throw new BadRequestException("Invalid e-mail");
        }
    }

    public void validateIfUserAlreadyExists(User user) {
        Optional<User> doesUserAlreadyExists = this.userDataProvider.findByEmail(
                user.getEmail()
        );

        if (doesUserAlreadyExists.isPresent()) {
            throw new ConflictException("User already exists");
        }
    }

    public String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
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
