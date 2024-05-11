package com.root.pattern.application.factory.music;

import com.root.pattern.adapter.repository.AlbumDataProviderImpl;
import com.root.pattern.adapter.repository.CategoryDataProviderImpl;
import com.root.pattern.adapter.repository.MusicDataProviderImpl;
import com.root.pattern.adapter.repository.MusicianDataProviderImpl;
import com.root.pattern.domain.usecase.music.RegisterMusicUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class RegisterMusicFactory {
    private final MusicianDataProviderImpl musicianDataProvider;
    private final AlbumDataProviderImpl albumDataProvider;
    private final CategoryDataProviderImpl categoryDataProvider;
    private final MusicDataProviderImpl musicDataProvider;

    @Bean("RegisterMusicUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RegisterMusicUsecase create() {
        return RegisterMusicUsecase.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .albumDataProvider(this.albumDataProvider)
            .categoryDataProvider(this.categoryDataProvider)
            .musicDataProvider(this.musicDataProvider)
            .build();
    }
}
