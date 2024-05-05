package com.root.pattern.application.factory.music;

import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.usecase.FilterOneMusicUsecase;
import com.root.pattern.domain.usecase.music.FilterOneMusicUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class FilterOneMusicFactory {
    private final MusicDataProvider musicDataProvider;

    @Bean("FilterOneMusicUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FilterOneMusicUsecase create() {
        return FilterOneMusicUsecaseImpl.builder()
            .musicDataProvider(this.musicDataProvider)
            .build();
    }
}
