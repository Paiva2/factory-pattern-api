package com.root.pattern.application.factory.playlist;

import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.usecase.playlist.EditMusicOrderPlaylistUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class EditMusicOrderPlaylistFactory {
    private final UserDataProvider userDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;
    private final PlaylistDataProvider playlistDataProvider;

    @Bean("EditMusicOrderPlaylistUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EditMusicOrderPlaylistUsecase create() {
        return EditMusicOrderPlaylistUsecase.builder()
            .userDataProvider(this.userDataProvider)
            .playlistMusicDataProvider(this.playlistMusicDataProvider)
            .playlistDataProvider(this.playlistDataProvider)
            .build();
    }
}
