package com.root.pattern.domain.usecase.user;

import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.UserDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class GetUserProfileUsecase {
    private final UserDataProvider userDataProvider;

    public void exec(User user) {

    }
}
