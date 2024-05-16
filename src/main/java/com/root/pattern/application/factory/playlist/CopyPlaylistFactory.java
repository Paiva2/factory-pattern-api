package com.root.pattern.application.factory.playlist;

import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.usecase.playlist.CopyPlaylistUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class CopyPlaylistFactory {
    private final PlaylistDataProvider playlistDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;
    private final UserDataProvider userDataProvider;

    @Bean("CopyPlaylistUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CopyPlaylistUsecase create() {
        return CopyPlaylistUsecase.builder()
            .playlistDataProvider(this.playlistDataProvider)
            .playlistMusicDataProvider(this.playlistMusicDataProvider)
            .userDataProvider(this.userDataProvider)
            .build();
    }
}
