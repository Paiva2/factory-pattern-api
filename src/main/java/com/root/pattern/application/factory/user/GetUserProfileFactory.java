package com.root.pattern.application.factory.user;

import com.root.pattern.adapter.repository.UserDataProviderImpl;
import com.root.pattern.domain.usecase.user.GetUserProfileUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class GetUserProfileFactory {
    private final UserDataProviderImpl userDataProvider;

    @Bean("GetUserProfileUseCase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GetUserProfileUsecase create() {
        return GetUserProfileUsecase.builder()
            .userDataProvider(this.userDataProvider)
            .build();
    }
}
