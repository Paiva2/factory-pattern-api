package com.root.pattern.application.factory.music;

import com.root.pattern.domain.interfaces.repository.CategoryDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.strategy.CopyPropertiesStrategy;
import com.root.pattern.domain.usecase.music.UpdateMusicUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class UpdateMusicFactory {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;
    private final CopyPropertiesStrategy copyPropertiesStrategy;
    private final CategoryDataProvider categoryDataProvider;

    @Bean("UpdateMusicUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UpdateMusicUsecase create() {
        return UpdateMusicUsecase.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .musicDataProvider(this.musicDataProvider)
            .copyPropertiesStrategy(this.copyPropertiesStrategy)
            .categoryDataProvider(this.categoryDataProvider)
            .build();
    }
}
