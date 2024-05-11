package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.user.GetUserProfileUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;

@AllArgsConstructor
@Builder
public class GetUserProfileUsecaseImpl implements GetUserProfileUsecase {
    private final UserDataProvider userDataProvider;

    @Override
    public UserOutputDTO exec(Long userId) {
        this.inputValidations(userId);
        User user = this.validateIfUserExists(userId);

        return this.mountOutput(user);
    }

    @Override
    public void inputValidations(Long userId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
        }
    }

    @Override
    public User validateIfUserExists(Long userId) {
        User user = this.userDataProvider.findById(userId)
            .orElseThrow(() -> new NotFoundException("User"));

        return user;
    }

    @Override
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
