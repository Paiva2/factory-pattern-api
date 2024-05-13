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
import com.root.pattern.domain.entity.PlaylistMusic;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
@Builder
public class DeleteMusicFromPlaylistUsecase {
    private final UserDataProvider userDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;
    private final PlaylistDataProvider playlistDataProvider;

    @Transactional
    public GetPlaylistOutputDTO exec(Long userId, UUID playlistMusicId) {
        this.validateInputs(userId, playlistMusicId);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        PlaylistMusic playlistMusic = this.checkIfPlaylistMusicExists(playlistMusicId);
        this.checkIfPlaylistMusicBelongToUser(user, playlistMusic);

        Playlist playlist = playlistMusic.getPlaylist();
        this.reOrganizeAllMusicOrdersOnPlaylist(playlist, playlistMusic.getMusicPlaylistOrder());

        this.deleteMusicFromPlaylist(playlistMusic.getId());

        Playlist updatedPlaylist = this.updatedPlaylist(playlist.getId());

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

    private void checkIfPlaylistMusicBelongToUser(User user, PlaylistMusic playlistMusic) {
        Long userId = user.getId();
        Long playlistMusicUserId = playlistMusic.getPlaylist().getUser().getId();

        if (!playlistMusicUserId.equals(userId)) {
            throw new ForbiddenException("Playlist music do not belong to provided user");
        }
    }

    private void reOrganizeAllMusicOrdersOnPlaylist(Playlist playlist, Integer positionBeingRemoved) {
        this.playlistMusicDataProvider.decreaseAllOrderFromMusicOnPlaylist(playlist.getId(), positionBeingRemoved);
    }

    private void deleteMusicFromPlaylist(UUID playlistMusicId) {
        this.playlistMusicDataProvider.delete(playlistMusicId);
    }

    private Playlist updatedPlaylist(UUID playlistId) {
        return this.playlistDataProvider.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist"));
    }

    private GetPlaylistOutputDTO mountOutput(Playlist playlist) {
        return GetPlaylistOutputDTO.builder()
            .id(playlist.getId())
            .name(playlist.getName())
            .order(playlist.getOrder())
            .createdAt(playlist.getCreatedAt())
            .coverImage(playlist.getCoverImage())
            .updatedAt(playlist.getUpdatedAt())
            .user(UserOutputDTO.builder()
                .id(playlist.getUser().getId())
                .name(playlist.getUser().getName())
                .email(playlist.getUser().getEmail())
                .role(playlist.getUser().getRole())
                .createdAt(playlist.getUser().getCreatedAt())
                .build()
            )
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
                        .isSingle(playlistMusic.getMusic().getIsSingle())
                        .order(Objects.nonNull(playlistMusic.getMusic().getAlbumOrder()) ? Math.toIntExact(playlistMusic.getMusic().getAlbumOrder()) : null)
                        .disabled(playlistMusic.getMusic().getDisabled())
                        .createdAt(playlistMusic.getMusic().getCreatedAt())
                        .build()
                    )
                    .build()
            ).collect(Collectors.toList()))
            .build();
    }
}
