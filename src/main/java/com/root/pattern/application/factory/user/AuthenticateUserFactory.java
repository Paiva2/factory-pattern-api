package com.root.pattern.application.factory.user;

import com.root.pattern.adapter.repository.UserDataProviderImpl;
import com.root.pattern.domain.usecase.user.AuthenticateUserUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Configuration
public class AuthenticateUserFactory {
    private final UserDataProviderImpl userDataProvider;
    private final PasswordEncoder passwordEncoder;

    @Bean("AuthenticateUserUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AuthenticateUserUsecase create() {
        return AuthenticateUserUsecase.builder()
            .userDataProvider(this.userDataProvider)
            .passwordEncoder(passwordEncoder)
            .build();
    }
}
