package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.strategy.context.MailValidator;
import com.root.pattern.domain.strategy.context.PropertiesCopier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Builder
public class UpdateUserProfileUsecase {
    private final UserDataProvider userDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailValidator mailValidator;
    private final PropertiesCopier propertiesCopier;

    public UserOutputDTO exec(User user) {
        this.validateInputs(user);

        User getUser = this.checkIfUserExists(user.getId());

        if (Objects.nonNull(getUser.getDisabledAt())) {
            throw new ForbiddenException("User disabled");
        }

        if (Objects.nonNull(user.getEmail())) {
            this.checkIfEmailAlreadyExists(user.getEmail(), getUser);
        }

        if (Objects.nonNull(user.getPassword())) {
            user.setPassword(this.hashNewPassword(user.getPassword()));
        }

        this.propertiesCopier.copyNonNullProps(user, getUser);

        User updateUser = this.userDataProvider.update(getUser);

        return this.mountOutput(updateUser);
    }

    public void validateInputs(User user) {
        if (Objects.nonNull(user.getEmail())) {
            this.validateEmail(user.getEmail());
        }

        if (Objects.nonNull(user.getPassword()) && user.getPassword().length() < 6) {
            throw new BadRequestException("New password must have at least 6 characters");
        }
    }

    public void validateEmail(String email) {
        if (!this.mailValidator.validate(email)) {
            throw new BadRequestException("Invalid new e-mail");
        }
    }

    public void checkIfEmailAlreadyExists(String email, User user) {
        Optional<User> findUser = this.userDataProvider.findByEmail(email);

        if (findUser.isPresent()) {
            boolean doesUserFoundIsMe = findUser.get().getId().equals(user.getId());

            if (doesUserFoundIsMe) return;

            throw new ConflictException("E-mail already being used");
        }
    }

    public User checkIfUserExists(Long id) {
        return this.userDataProvider.findById(id)
            .orElseThrow(() -> new NotFoundException("User"));
    }

    public String hashNewPassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    public UserOutputDTO mountOutput(User user) {
        return UserOutputDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
