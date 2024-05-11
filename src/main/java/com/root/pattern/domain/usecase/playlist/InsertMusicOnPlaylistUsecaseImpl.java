package com.root.pattern.domain.usecase.playlist;

import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.entity.PlaylistMusic;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.music.InsertMusicOnPlaylistUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

//todo: tests
@AllArgsConstructor
@Builder
public class InsertMusicOnPlaylistUsecaseImpl implements InsertMusicOnPlaylistUsecase {
    private final MusicDataProvider musicDataProvider;
    private final UserDataProvider userDataProvider;
    private final PlaylistDataProvider playlistDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;

    @Override
    public void exec(Long userId, UUID musicId, UUID playlistId) {
        this.validateInputs(userId, musicId, playlistId);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        Playlist playlist = this.checkIfPlaylistExists(playlistId);
        this.checkIfPlaylistIsDisabled(playlist);

        Music music = this.checkIfMusicExists(musicId);
        this.checkIfMusicIsDisabled(music);

        this.checkIfPlaylistBelongsToUser(user, playlist);
        this.checkIfPlaylistAlreadyHasMusic(music.getId(), playlist.getId());

        this.insertMusicOnPlaylist(music, playlist);
    }

    @Override
    public void validateInputs(Long userId, UUID musicId, UUID playlistId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
        }

        if (Objects.isNull(musicId)) {
            throw new BadRequestException("Music id can't be empty");
        }

        if (Objects.isNull(playlistId)) {
            throw new BadRequestException("Playlist id can't be empty");
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
    public Playlist checkIfPlaylistExists(UUID playlistId) {
        return this.playlistDataProvider.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist"));
    }

    @Override
    public void checkIfPlaylistIsDisabled(Playlist playlist) {
        if (playlist.isDisabled()) {
            throw new ForbiddenException("Playlist is disabled");
        }
    }

    @Override
    public Music checkIfMusicExists(UUID musicId) {
        return this.musicDataProvider.findById(musicId).orElseThrow(() -> new NotFoundException("Music"));
    }

    @Override
    public void checkIfMusicIsDisabled(Music music) {
        if (music.isDisabled()) {
            throw new ForbiddenException("Music is disabled");
        }
    }

    @Override
    public Long getLastMusicOrderOnPlaylist(UUID playlistId) {
        return this.playlistMusicDataProvider.findLastMusicOrder(playlistId);
    }

    @Override
    public void checkIfPlaylistBelongsToUser(User user, Playlist playlist) {
        Long userId = user.getId();
        Long playlistUserId = playlist.getUser().getId();

        if (!userId.equals(playlistUserId)) {
            throw new ForbiddenException("Playlist do not belong to provided user");
        }
    }

    @Override
    public void checkIfPlaylistAlreadyHasMusic(UUID musicId, UUID playlistId) {
        Optional<PlaylistMusic> playlistMusicExistent = this.playlistMusicDataProvider.findByPlaylistAndMusic(playlistId, musicId);

        if (playlistMusicExistent.isPresent()) {
            throw new ConflictException("Playlist already has this music added");
        }
    }

    @Override
    public void insertMusicOnPlaylist(Music music, Playlist playlist) {
        Long lastMusicOrder = this.getLastMusicOrderOnPlaylist(playlist.getId());
        Long newMusicOrder = Objects.isNull(lastMusicOrder) ? 0 : lastMusicOrder;

        PlaylistMusic playlistMusic = new PlaylistMusic();
        playlistMusic.setMusic(music);
        playlistMusic.setPlaylist(playlist);
        playlistMusic.setMusicPlaylistOrder(Math.toIntExact(newMusicOrder) + 1);
        playlistMusic.setDisabled(false);

        this.playlistMusicDataProvider.register(playlistMusic);
    }
}
