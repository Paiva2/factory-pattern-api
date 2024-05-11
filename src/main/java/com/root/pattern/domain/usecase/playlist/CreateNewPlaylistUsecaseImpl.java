package com.root.pattern.domain.usecase.playlist;

import com.root.pattern.adapter.dto.playlist.NewPlaylistOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.playlist.CreateNewPlaylistUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.Optional;

//TODO: TESTS
//TODO: UPLOAD COVER IMAGE
@AllArgsConstructor
@Builder
public class CreateNewPlaylistUsecaseImpl implements CreateNewPlaylistUsecase {
    private final UserDataProvider userDataProvider;
    private final PlaylistDataProvider playlistDataProvider;

    @Override
    public NewPlaylistOutputDTO exec(Long userId, Playlist playlist) {
        this.inputsValidation(userId, playlist);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        this.checkIfUserHasSamePlaylist(playlist.getName(), user.getId());
        Integer newPlaylistOrder = this.lastOrderedPlaylist(user.getId());

        playlist.setUser(user);

        playlist.setOrder(newPlaylistOrder + 1);

        Playlist newPlaylist = this.persistNewPlaylist(playlist);

        return this.mountOutput(newPlaylist);
    }

    @Override
    public void inputsValidation(Long userId, Playlist playlist) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
        }

        if (Objects.isNull(playlist)) {
            throw new BadRequestException("Playlist can't be empty");
        }

        if (Objects.isNull(playlist.getName())) {
            throw new BadRequestException("Playlist name can't be empty");
        }
    }

    @Override
    public User checkIfUserExists(Long userId) {
        return this.userDataProvider.findById(userId).orElseThrow(() -> new NotFoundException("User"));
    }

    @Override
    public void checkIfUserIsDisabled(User user) {
        if (user.isDisabled()) {
            throw new ForbiddenException("User is disabled");
        }
    }

    @Override
    public void checkIfUserHasSamePlaylist(String playlistName, Long userId) {
        Optional<Playlist> playlist = this.playlistDataProvider.findUserActivesByName(playlistName, userId);

        if (playlist.isPresent()) {
            throw new ConflictException("User already has a playlist with this name");
        }
    }

    @Override
    public Integer lastOrderedPlaylist(Long userId) {
        Integer lastOrdered = this.playlistDataProvider.getLastOrderedPlaylistFromUser(userId);

        return Objects.isNull(lastOrdered) ? 0 : lastOrdered;
    }

    @Override
    public Playlist persistNewPlaylist(Playlist playlist) {
        return this.playlistDataProvider.create(playlist);
    }

    @Override
    public NewPlaylistOutputDTO mountOutput(Playlist playlist) {
        return NewPlaylistOutputDTO.builder()
            .id(playlist.getId())
            .name(playlist.getName())
            .coverImage(playlist.getCoverImage())
            .createdAt(playlist.getCreatedAt())
            .order(playlist.getOrder())
            .build();
    }
}
