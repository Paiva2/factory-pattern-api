package com.root.pattern.application.factory.musician;

import com.root.pattern.adapter.repository.MusicianDataProviderImpl;
import com.root.pattern.domain.usecase.musician.AuthMusicianUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class AuthMusicianFactory {
    private final MusicianDataProviderImpl musicianDataProvider;
    private final PasswordEncoder passwordEncoder;

    @Bean("AuthMusicianUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AuthMusicianUsecaseImpl create() {
        return AuthMusicianUsecaseImpl.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .build();
    }
}
