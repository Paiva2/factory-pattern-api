package com.root.pattern.application.factory.album;

import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.usecase.album.RemoveMusicFromAlbumUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class RemoveMusicFromAlbumFactory {
    private final AlbumDataProvider albumDataProvider;
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Bean("RemoveMusicFromAlbumUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RemoveMusicFromAlbumUsecase create() {
        return RemoveMusicFromAlbumUsecase.builder()
            .albumDataProvider(this.albumDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .musicDataProvider(this.musicDataProvider)
            .build();
    }
}
