package com.root.pattern.application.factory.user;

import com.root.pattern.adapter.repository.UserDataProviderImpl;
import com.root.pattern.domain.strategy.concrete.CopyPropertiesBeanUtilStrategy;
import com.root.pattern.domain.strategy.concrete.EmailValidatorRegexStrategy;
import com.root.pattern.domain.strategy.context.MailValidator;
import com.root.pattern.domain.strategy.context.PropertiesCopier;
import com.root.pattern.domain.usecase.user.UpdateUserProfileUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class UpdateUserProfileFactory {
    private final UserDataProviderImpl userDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailValidator mailValidator;
    private final PropertiesCopier propertiesCopier;

    private final EmailValidatorRegexStrategy emailValidatorRegexStrategy;
    private final CopyPropertiesBeanUtilStrategy copyPropertiesBeanUtilStrategy;

    @Bean("UpdateUserProfileUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UpdateUserProfileUsecaseImpl create() {
        return UpdateUserProfileUsecaseImpl.builder()
            .userDataProvider(this.userDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .mailValidator(this.mailValidatorStrategy())
            .propertiesCopier(this.propertiesCopierStrategy())
            .build();
    }

    private MailValidator mailValidatorStrategy() {
        MailValidator validatorStrategy = this.mailValidator;
        validatorStrategy.setEmailValidatorStrategy(this.emailValidatorRegexStrategy);

        return validatorStrategy;
    }

    private PropertiesCopier propertiesCopierStrategy() {
        PropertiesCopier propertiesCopierStrategy = this.propertiesCopier;
        propertiesCopierStrategy.setCopyPropertiesStrategy(this.copyPropertiesBeanUtilStrategy);

        return propertiesCopierStrategy;
    }
}
