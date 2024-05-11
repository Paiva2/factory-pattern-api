package com.root.pattern.application.factory.musician;

import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.musician.FilterMusicianUsecase;
import com.root.pattern.domain.usecase.musician.FilterMusicianUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class FilterMusicianFactory {
    private final MusicianDataProvider musicianDataProvider;

    @Bean("FilterMusicianUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FilterMusicianUsecase create() {
        return FilterMusicianUsecaseImpl.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .build();
    }
}
