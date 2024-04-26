package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.domain.entity.User;

public interface GetUserProfileUsecase {
    UserOutputDTO exec(User newUser);
}
