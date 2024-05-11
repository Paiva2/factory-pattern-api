package com.root.pattern.application.factory.music;

import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.usecase.music.FilterMusicsNameUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class FilterMusicsNameFactory {
    private final MusicDataProvider musicDataProvider;

    @Bean("FilterMusicsNameUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FilterMusicsNameUsecase create() {
        return FilterMusicsNameUsecase.builder()
            .musicDataProvider(this.musicDataProvider)
            .build();
    }
}
