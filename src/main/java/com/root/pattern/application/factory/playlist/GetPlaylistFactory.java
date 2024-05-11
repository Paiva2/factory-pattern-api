package com.root.pattern.application.factory.playlist;

import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import com.root.pattern.domain.usecase.playlist.GetPlaylistUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class GetPlaylistFactory {
    private final PlaylistDataProvider playlistDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;

    @Bean("GetPlaylistUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GetPlaylistUsecase create() {
        return GetPlaylistUsecase.builder()
            .playlistDataProvider(this.playlistDataProvider)
            .playlistMusicDataProvider(this.playlistMusicDataProvider)
            .build();
    }
}
