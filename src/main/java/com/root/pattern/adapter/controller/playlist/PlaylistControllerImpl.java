package com.root.pattern.adapter.controller.playlist;

import com.root.pattern.adapter.dto.playlist.GetPlaylistOutputDTO;
import com.root.pattern.adapter.dto.playlist.ListOwnPlaylistsOutputDTO;
import com.root.pattern.adapter.dto.playlist.NewPlaylistInputDTO;
import com.root.pattern.adapter.dto.playlist.NewPlaylistOutputDTO;
import com.root.pattern.domain.interfaces.usecase.CreateNewPlaylistUsecase;
import com.root.pattern.domain.interfaces.usecase.GetPlaylistUsecase;
import com.root.pattern.domain.interfaces.usecase.InsertMusicOnPlaylistUsecase;
import com.root.pattern.domain.interfaces.usecase.ListOwnPlaylistsUsecase;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class PlaylistControllerImpl implements PlaylistController {
    private final CreateNewPlaylistUsecase createNewPlaylistUsecase;
    private final ListOwnPlaylistsUsecase listOwnPlaylistsUsecase;
    private final GetPlaylistUsecase getPlaylistUsecase;
    private final InsertMusicOnPlaylistUsecase insertMusicOnPlaylistUsecase;

    @Override
    public ResponseEntity<NewPlaylistOutputDTO> create(
        Authentication authentication,
        @RequestBody @Valid NewPlaylistInputDTO dto
    ) {
        Long userId = Long.valueOf(authentication.getName());
        NewPlaylistOutputDTO output = this.createNewPlaylistUsecase.exec(userId, dto.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @Override
    public ResponseEntity<ListOwnPlaylistsOutputDTO> listOwn(
        Authentication authentication,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer perPage,
        @RequestParam(value = "name", required = false) String name
    ) {
        Long userId = Long.valueOf(authentication.getName());
        ListOwnPlaylistsOutputDTO output = this.listOwnPlaylistsUsecase.exec(userId, page, perPage, name);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<GetPlaylistOutputDTO> get(
        @PathVariable("playlistId") UUID playlistId
    ) {
        GetPlaylistOutputDTO output = this.getPlaylistUsecase.exec(playlistId);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<Void> newMusic(
        Authentication authentication,
        @PathVariable("playlistId") UUID playlistId,
        @PathVariable("musicId") UUID musicId
    ) {
        Long userId = Long.valueOf(authentication.getName());
        this.insertMusicOnPlaylistUsecase.exec(userId, musicId, playlistId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
