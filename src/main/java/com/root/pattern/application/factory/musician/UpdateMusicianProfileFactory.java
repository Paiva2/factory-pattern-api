package com.root.pattern.application.factory.musician;

import com.root.pattern.adapter.repository.MusicianDataProviderImpl;
import com.root.pattern.domain.strategy.concrete.CopyPropertiesBeanUtilStrategy;
import com.root.pattern.domain.strategy.concrete.EmailValidatorRegexStrategy;
import com.root.pattern.domain.strategy.context.MailValidator;
import com.root.pattern.domain.strategy.context.PropertiesCopier;
import com.root.pattern.domain.usecase.musician.UpdateMusicianProfileUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateMusicianProfileFactory {
    private final MusicianDataProviderImpl musicianDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailValidator mailValidator;
    private final PropertiesCopier propertiesCopier;

    private final EmailValidatorRegexStrategy emailValidatorRegexStrategy;
    private final CopyPropertiesBeanUtilStrategy copyPropertiesBeanUtilStrategy;

    @Bean("UpdateMusicianProfileUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UpdateMusicianProfileUsecase create() {
        return UpdateMusicianProfileUsecase.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .mailValidator(this.mailValidatorStrategy())
            .propertiesCopier(this.propertiesCopierStrategy())
            .passwordEncoder(this.passwordEncoder)
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
