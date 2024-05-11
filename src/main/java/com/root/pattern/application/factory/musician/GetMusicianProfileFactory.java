package com.root.pattern.application.factory.musician;

import com.root.pattern.adapter.repository.MusicianDataProviderImpl;
import com.root.pattern.domain.usecase.musician.GetMusicianProfileUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class GetMusicianProfileFactory {
    private final MusicianDataProviderImpl musicianDataProvider;

    @Bean("GetMusicianProfileUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GetMusicianProfileUsecase create() {
        return GetMusicianProfileUsecase.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .build();
    }
}
