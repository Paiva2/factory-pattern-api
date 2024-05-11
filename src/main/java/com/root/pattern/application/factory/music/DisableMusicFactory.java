package com.root.pattern.application.factory.music;

import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import com.root.pattern.domain.interfaces.usecase.music.DisableMusicUsecase;
import com.root.pattern.domain.usecase.music.DisableMusicUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class DisableMusicFactory {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;

    @Bean("DisableMusicUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DisableMusicUsecase create() {
        return DisableMusicUsecaseImpl.builder()
            .musicDataProvider(this.musicDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .playlistMusicDataProvider(this.playlistMusicDataProvider)
            .build();
    }
}
