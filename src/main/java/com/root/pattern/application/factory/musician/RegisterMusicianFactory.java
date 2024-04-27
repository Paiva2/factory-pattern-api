package com.root.pattern.application.factory.musician;

import com.root.pattern.adapter.repository.MusicianDataProviderImpl;
import com.root.pattern.domain.strategy.concrete.EmailValidatorRegexStrategy;
import com.root.pattern.domain.strategy.context.MailValidator;
import com.root.pattern.domain.usecase.musician.RegisterMusicianUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class RegisterMusicianFactory {
    private final MusicianDataProviderImpl musicianDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailValidator mailValidator;
    private final EmailValidatorRegexStrategy emailValidatorRegexStrategy;

    @Bean("RegisterMusicianUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RegisterMusicianUsecaseImpl create() {
        return RegisterMusicianUsecaseImpl.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .mailValidator(this.mailValidatorStrategy())
            .build();
    }

    private MailValidator mailValidatorStrategy() {
        this.mailValidator.setEmailValidatorStrategy(this.emailValidatorRegexStrategy);

        return this.mailValidator;
    }
}
