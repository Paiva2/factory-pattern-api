package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.UserDataProvider;
import com.root.pattern.domain.strategy.context.MailValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Builder
@AllArgsConstructor
public class RegisterUserUseCase {
    private final UserDataProvider userDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailValidator mailValidator;

    public User exec(User newUser) {
        this.useCaseValidations(newUser);

        Optional<User> doesUserAlreadyExists = this.userDataProvider.userExists(
                newUser.getEmail()
        );

        if (doesUserAlreadyExists.isPresent()) {
            throw new ConflictException("User already exists");
        }

        String hashNewPassword = this.passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashNewPassword);

        return this.userDataProvider.register(newUser);
    }

    private void useCaseValidations(User dto) {
        if (dto == null) {
            throw new BadRequestException("User can't be empty");
        }

        if (dto.getEmail() == null) {
            throw new BadRequestException("User e-mail can't be empty");
        }

        if (dto.getName() == null) {
            throw new BadRequestException("Name can't be empty");
        }

        if (dto.getPassword().length() < 6) {
            throw new BadRequestException("Password must have at least 6 characters");
        }

        if (!this.mailValidator.validate(dto.getEmail())) {
            throw new BadRequestException("Invalid e-mail");
        }
    }
}
