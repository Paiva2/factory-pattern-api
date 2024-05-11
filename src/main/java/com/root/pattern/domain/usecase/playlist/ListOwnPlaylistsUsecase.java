package com.root.pattern.domain.usecase.playlist;

import com.root.pattern.adapter.dto.playlist.ListOwnPlaylistsOutputDTO;
import com.root.pattern.adapter.dto.playlist.PlaylistDetailsOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//TODO: TESTS
@AllArgsConstructor
@Builder
public class ListOwnPlaylistsUsecase {
    private final UserDataProvider userDataProvider;
    private final PlaylistDataProvider playlistDataProvider;

    public ListOwnPlaylistsOutputDTO exec(Long userId, Integer page, Integer perPage, String name) {
        this.validateInputs(userId);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        List<Playlist> playlists = this.getAllPlaylists(userId, name, page, perPage);
        Long totalPlaylists = this.countPlaylists(userId, name);

        return this.mountOutput(playlists, page, perPage, totalPlaylists);
    }

    public void validateInputs(Long userId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
        }
    }

    public User checkIfUserExists(Long userId) {
        return this.userDataProvider.findById(userId).orElseThrow(() -> new NotFoundException("User"));
    }

    public void checkIfUserIsDisabled(User user) {
        if (user.isDisabled()) {
            throw new ForbiddenException("User is disabled");
        }
    }

    public List<Playlist> getAllPlaylists(Long userId, String name, Integer page, Integer perPage) {
        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 50) {
            perPage = 50;
        }

        Integer offSet = (page - 1) * perPage;

        return this.playlistDataProvider.getAllActivePlaylists(userId, offSet, perPage, name);
    }

    public Long countPlaylists(Long userId, String name) {
        return this.playlistDataProvider.countActivePlaylists(userId, name);
    }

    public ListOwnPlaylistsOutputDTO mountOutput(List<Playlist> playlists, Integer page, Integer perPage, Long totalItems) {
        return ListOwnPlaylistsOutputDTO.builder()
            .page(page)
            .perPage(perPage)
            .totalPlaylists(totalItems)
            .playlists(playlists.stream().map(playlist ->
                PlaylistDetailsOutputDTO.builder()
                    .id(playlist.getId())
                    .name(playlist.getName())
                    .coverImage(playlist.getCoverImage())
                    .totalMusics(playlist.getPlaylistMusics().size())
                    .createdAt(playlist.getCreatedAt())
                    .order(playlist.getOrder())
                    .build()
            ).collect(Collectors.toList()))
            .build();
    }
}
