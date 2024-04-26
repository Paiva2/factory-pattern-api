package com.root.pattern.domain.usecase.user.authenticateUserUsecase;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.domain.entity.User;

import java.util.Date;

public interface AuthenticateUserUsecase {
    UserOutputDTO exec(User user);

    void inputValidations(User user);

    User validateIfUserExists(String userEmail);

    void validateIfUserIsDisabled(Date disabledAt);

    void checkCredentials(String rawPass, String hashedPass);

    UserOutputDTO mountOutputDto(User user);
}
