package com.root.pattern.domain.interfaces.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.domain.entity.User;

public interface RegisterUserUsecase {
    UserOutputDTO exec(User newUser);

    void inputValidations(User dto);

    void validateIfUserAlreadyExists(User user);

    String encodePassword(String password);

    UserOutputDTO mountOutputDto(User user);
}
