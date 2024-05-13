package com.root.pattern.domain.usecase.playlist;

import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.adapter.dto.playlist.GetPlaylistOutputDTO;
import com.root.pattern.adapter.dto.playlistMusics.PlaylistMusicOutputDTO;
import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO: TESTS
@AllArgsConstructor
@Builder
public class GetPlaylistUsecase {
    private final PlaylistDataProvider playlistDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;

    public GetPlaylistOutputDTO exec(UUID playlistId) {
        this.validateInputs(playlistId);

        Playlist playlist = this.checkIfPlaylistExists(playlistId);
        this.checkIfPlaylistIsDisabled(playlist);

        return this.mountOutput(playlist);
    }

    public void validateInputs(UUID playlistId) {
        if (Objects.isNull(playlistId)) {
            throw new BadRequestException("Playlist id can't be empty");
        }
    }

    public Playlist checkIfPlaylistExists(UUID playlistId) {
        return this.playlistDataProvider.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist"));
    }

    public void checkIfPlaylistIsDisabled(Playlist playlist) {
        if (playlist.isDisabled()) {
            throw new ForbiddenException("Playlist is disabled");
        }
    }

    public GetPlaylistOutputDTO mountOutput(Playlist playlist) {
        return GetPlaylistOutputDTO.builder()
            .id(playlist.getId())
            .order(playlist.getOrder())
            .name(playlist.getName())
            .coverImage(playlist.getCoverImage())
            .user(UserOutputDTO.builder()
                .id(playlist.getUser().getId())
                .name(playlist.getUser().getName())
                .email(playlist.getUser().getEmail())
                .role(playlist.getUser().getRole())
                .createdAt(playlist.getUser().getCreatedAt())
                .build()
            )
            .createdAt(playlist.getCreatedAt())
            .updatedAt(playlist.getUpdatedAt())
            .musics(playlist.getPlaylistMusics().stream().map(playlistMusic ->
                PlaylistMusicOutputDTO.builder()
                    .id(playlistMusic.getId())
                    .createdAt(playlistMusic.getCreatedAt())
                    .orderPlaylist(playlistMusic.getMusicPlaylistOrder())
                    .music(MusicOutputDTO.builder()
                        .id(playlistMusic.getMusic().getId())
                        .duration(playlistMusic.getMusic().getDuration())
                        .name(playlistMusic.getMusic().getName())
                        .category(CategoryOutputDTO.builder()
                            .id(playlistMusic.getMusic().getCategory().getId())
                            .name(playlistMusic.getMusic().getCategory().getName().name())
                            .build()
                        )
                        .disabled(playlistMusic.getDisabled())
                        .order(playlistMusic.getMusicPlaylistOrder())
                        .isSingle(playlistMusic.getMusic().getIsSingle())
                        .createdAt(playlistMusic.getMusic().getCreatedAt())
                        .build()
                    )
                    .build()
            ).collect(Collectors.toList()))
            .build();
    }
}
