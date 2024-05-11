package com.root.pattern.application.factory.album;

import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.album.DisableAlbumUsecase;
import com.root.pattern.domain.usecase.album.DisableAlbumUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class DisableAlbumFactory {
    private final MusicianDataProvider musicianDataProvider;
    private final AlbumDataProvider albumDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Bean("DisableAlbumUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DisableAlbumUsecase create() {
        return DisableAlbumUsecaseImpl.builder()
            .albumDataProvider(this.albumDataProvider)
            .musicDataProvider(this.musicDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .build();
    }
}
