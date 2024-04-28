package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.domain.entity.User;

public interface UpdateUserProfileUsecase {
    UserOutputDTO exec(User user);

    void validateInputs(User user);

    void validateEmail(String email);

    void checkIfEmailAlreadyExists(String newEmail, User user);

    User checkIfUserExists(Long id);
    
    UserOutputDTO mountOutput(User user);

    String hashNewPassword(String password);
}
