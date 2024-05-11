package com.root.pattern.application.factory.music;

import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.music.InsertMusicOnAlbumUsecase;
import com.root.pattern.domain.usecase.album.InsertMusicOnAlbumUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class InsertMusicOnAlbumFactory {
    private final AlbumDataProvider albumDataProvider;
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Bean("InsertMusicOnAlbumUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public InsertMusicOnAlbumUsecase create() {
        return InsertMusicOnAlbumUsecaseImpl.builder()
            .albumDataProvider(this.albumDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .musicDataProvider(this.musicDataProvider)
            .build();
    }
}
