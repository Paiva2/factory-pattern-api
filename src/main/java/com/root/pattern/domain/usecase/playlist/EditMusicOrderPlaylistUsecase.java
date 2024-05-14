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
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

//todo: tests
@AllArgsConstructor
@Builder
public class EditMusicOrderPlaylistUsecase {
    private final UserDataProvider userDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;
    private final PlaylistDataProvider playlistDataProvider;

    @Transactional
    public GetPlaylistOutputDTO exec(Long userId, UUID playlistMusicId, Integer newOrder) {
        this.validateInputs(userId, playlistMusicId);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        PlaylistMusic playlistMusic = this.checkIfPlaylistMusicExists(playlistMusicId);
        this.checkIfPlaylistMusicIsDisabled(playlistMusic);

        Playlist playlist = playlistMusic.getPlaylist();
        this.checkIfPlaylistBelongToUser(user, playlist);

        if (newOrder.equals(playlistMusic.getMusicPlaylistOrder())) {
            throw new ConflictException("Music is already on this order in playlist");
        }

        this.checkIfPositionIsValidOnPlaylist(playlist.getId(), newOrder);
        this.handleReorderPlaylistMusics(playlist.getId(), newOrder, playlistMusic.getMusicPlaylistOrder());

        playlistMusic.setMusicPlaylistOrder(newOrder);

        this.savePlaylistMusic(playlistMusic);

        Playlist updatedPlaylist = this.getUpdatedPlaylist(playlist.getId());

        return this.mountOutput(updatedPlaylist);
    }

    private void validateInputs(Long userId, UUID playlistMusicId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
        }

        if (Objects.isNull(playlistMusicId)) {
            throw new BadRequestException("Playlist music id can't be empty");
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

    private PlaylistMusic checkIfPlaylistMusicExists(UUID playlistMusicId) {
        return this.playlistMusicDataProvider.findById(playlistMusicId).orElseThrow(() -> new NotFoundException("Playlist music"));
    }

    private void checkIfPlaylistMusicIsDisabled(PlaylistMusic playlistMusic) {
        if (playlistMusic.getDisabled()) {
            throw new ForbiddenException("Playlist music is disabled");
        }
    }

    private void checkIfPlaylistBelongToUser(User user, Playlist playlist) {
        Long userId = user.getId();
        Long playlistMusicUserId = playlist.getUser().getId();

        if (!userId.equals(playlistMusicUserId)) {
            throw new ForbiddenException("Playlist do not belong to provided user");
        }
    }

    private void checkIfPositionIsValidOnPlaylist(UUID playlistId, Integer newPosition) {
        Integer lastPosition = Math.toIntExact(this.playlistMusicDataProvider.findLastMusicOrder(playlistId));

        if (newPosition > lastPosition) {
            throw new ConflictException("New position provided can't be more than last position available");
        } else if (newPosition < 0) {
            throw new ConflictException("New position can't be less than 0");
        }
    }

    private void handleReorderPlaylistMusics(UUID playlistId, Integer orderFrom, Integer oldPosition) {
        this.playlistMusicDataProvider.increaseAllOrderFromMusicOnPlaylist(playlistId, orderFrom, oldPosition);
    }

    private void savePlaylistMusic(PlaylistMusic playlistMusic) {
        this.playlistMusicDataProvider.register(playlistMusic);
    }

    private Playlist getUpdatedPlaylist(UUID playlistId) {
        return this.playlistDataProvider.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist"));
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
