package com.root.pattern.application.factory.music;

import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.musician.FilterMusicianMusicsUsecase;
import com.root.pattern.domain.usecase.music.FilterMusicianMusicsUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class FilterMusicianMusicsFactory {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Bean("FilterMusicianMusicsUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FilterMusicianMusicsUsecase create() {
        return FilterMusicianMusicsUsecaseImpl.builder()
            .musicDataProvider(this.musicDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .build();
    }
}
