package com.root.pattern.domain.usecase.playlist;

import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.adapter.dto.playlist.GetPlaylistOutputDTO;
import com.root.pattern.adapter.dto.playlistMusics.PlaylistMusicOutputDTO;
import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.entity.PlaylistMusic;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

//todo: tests
@Builder
@AllArgsConstructor
public class CopyPlaylistUsecase {
    private final PlaylistDataProvider playlistDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;
    private final UserDataProvider userDataProvider;

    @Transactional
    public GetPlaylistOutputDTO exec(Long userId, UUID playlistCopiedId, String playlistName) {
        this.validateInputs(userId, playlistCopiedId);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        Playlist playlistCopied = this.checkIfPlaylistCopiedExists(playlistCopiedId);
        this.checkIfPlaylistCopiedIsDisabled(playlistCopied);

        Integer lastPositionOrder = this.lastPlaylistPositionOrder(user.getId());
        Integer newPlaylistOrder = lastPositionOrder < 1 ? 0 : lastPositionOrder + 1;

        Playlist newPlaylistByCopy = Playlist.builder()
            .name(playlistName)
            .coverImage(playlistCopied.getCoverImage())
            .order(newPlaylistOrder)
            .user(user)
            .build();

        this.handleRepeatedPlaylistName(user.getId(), newPlaylistByCopy);

        Playlist newPlaylistCreated = this.persistNewPlaylist(newPlaylistByCopy);

        Set<PlaylistMusic> musicsCopied = this.fillPlaylistMusicsByCopy(playlistCopied.getId(), newPlaylistCreated);
        newPlaylistCreated.setPlaylistMusics(musicsCopied);

        return this.mountOutput(newPlaylistCreated);
    }

    private void validateInputs(Long userId, UUID playlistCopiedId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
        }

        if (Objects.isNull(playlistCopiedId)) {
            throw new BadRequestException("Playlist copied id can't be empty");
        }
    }

    private User checkIfUserExists(Long userId) {
        return this.userDataProvider.findById(userId).orElseThrow(() -> new NotFoundException("User"));
    }

    private void checkIfUserIsDisabled(User user) {
        if (user.isDisabled()) {
            throw new ForbiddenException("User is disabled");
        }
    }

    private Playlist checkIfPlaylistCopiedExists(UUID playlistId) {
        return this.playlistDataProvider.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist"));
    }

    private void checkIfPlaylistCopiedIsDisabled(Playlist playlist) {
        if (playlist.isDisabled()) {
            throw new ForbiddenException("Playlist is disabled");
        }
    }

    private void handleRepeatedPlaylistName(Long userId, Playlist newPlaylistByCopy) {
        Optional<Playlist> doesUserHasPlaylist = this.playlistDataProvider.findUserActivesByName(newPlaylistByCopy.getName(), userId);

        if (doesUserHasPlaylist.isPresent()) {
            throw new ConflictException("A playlist with this name already exists");
        }
    }

    private Integer lastPlaylistPositionOrder(Long userId) {
        return this.playlistDataProvider.getLastOrderedPlaylistFromUser(userId);
    }

    private Set<PlaylistMusic> fillPlaylistMusicsByCopy(UUID copiedPlaylistId, Playlist newPlaylist) {
        Set<PlaylistMusic> musics = this.playlistMusicDataProvider.findAllByPlaylist(copiedPlaylistId);

        Set<PlaylistMusic> copyMusics = new HashSet<>();

        for (PlaylistMusic pm : musics) {
            copyMusics.add(PlaylistMusic.builder()
                .playlist(newPlaylist)
                .disabled(pm.getDisabled())
                .musicPlaylistOrder(pm.getMusicPlaylistOrder())
                .music(pm.getMusic())
                .disabledAt(pm.getDisabledAt())
                .build());
        }

        return this.playlistMusicDataProvider.registerAll(copyMusics);
    }

    private Playlist persistNewPlaylist(Playlist newPlaylist) {
        return this.playlistDataProvider.create(newPlaylist);
    }

    private GetPlaylistOutputDTO mountOutput(Playlist playlist) {
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
