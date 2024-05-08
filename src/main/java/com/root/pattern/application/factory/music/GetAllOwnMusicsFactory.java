package com.root.pattern.application.factory.music;

import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.GetAllOwnMusicsUsecase;
import com.root.pattern.domain.usecase.music.GetAllOwnMusicsUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class GetAllOwnMusicsFactory {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Bean("GetAllOwnMusicsUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GetAllOwnMusicsUsecase create() {
        return GetAllOwnMusicsUsecaseImpl.builder()
            .musicDataProvider(this.musicDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .build();
    }
}
