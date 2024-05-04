package com.root.pattern.application.factory.album;

import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.usecase.album.ListMusicianAlbumsUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class ListMusicianAlbumsFactory {
    private final MusicianDataProvider musicianDataProvider;
    private final AlbumDataProvider albumDataProvider;

    @Bean("ListMusicianAlbumsFactory")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public com.root.pattern.domain.interfaces.usecase.ListMusicianAlbumsUsecase create() {
        return ListMusicianAlbumsUsecaseImpl.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .albumDataProvider(this.albumDataProvider)
            .build();
    }
}
