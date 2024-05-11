package com.root.pattern.application.factory.playlist;

import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.usecase.playlist.InsertMusicOnPlaylistUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class InsertMusicOnPlaylistFactory {
    private final MusicDataProvider musicDataProvider;
    private final UserDataProvider userDataProvider;
    private final PlaylistDataProvider playlistDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;

    @Bean("InsertMusicOnPlaylistUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public InsertMusicOnPlaylistUsecase create() {
        return InsertMusicOnPlaylistUsecase.builder()
            .musicDataProvider(this.musicDataProvider)
            .userDataProvider(this.userDataProvider)
            .playlistDataProvider(this.playlistDataProvider)
            .playlistMusicDataProvider(this.playlistMusicDataProvider)
            .build();
    }
}
