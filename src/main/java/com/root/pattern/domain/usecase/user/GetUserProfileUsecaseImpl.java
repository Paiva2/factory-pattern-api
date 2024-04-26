package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.GetUserProfileUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class GetUserProfileUsecaseImpl implements GetUserProfileUsecase {
    private final UserDataProvider userDataProvider;
    
    @Override
    public UserOutputDTO exec(User newUser) {
        return null;
    }
}
