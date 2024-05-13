package com.root.pattern.adapter.controller.playlist;

import com.root.pattern.adapter.dto.playlist.GetPlaylistOutputDTO;
import com.root.pattern.adapter.dto.playlist.ListOwnPlaylistsOutputDTO;
import com.root.pattern.adapter.dto.playlist.NewPlaylistInputDTO;
import com.root.pattern.adapter.dto.playlist.NewPlaylistOutputDTO;
import com.root.pattern.domain.usecase.playlist.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final ExportPlaylistExcelUsecase exportPlaylistExcelUsecase;
    private final DeleteMusicFromPlaylistUsecase deleteMusicFromPlaylistUsecase;

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

    @Override
    public ResponseEntity<ByteArrayResource> export(
        @PathVariable("playlistId") UUID playlistId
    ) {
        ByteArrayResource output = this.exportPlaylistExcelUsecase.exec(playlistId);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Playlist-data.xlsx");

        return ResponseEntity.status(HttpStatus.OK).headers(header).body(output);
    }

    @Override
    public ResponseEntity<GetPlaylistOutputDTO> deletePlaylistMusic(
        Authentication authentication,
        @PathVariable("playlistMusicId") UUID playlistMusicId
    ) {
        Long userId = Long.valueOf(authentication.getName());
        GetPlaylistOutputDTO output = this.deleteMusicFromPlaylistUsecase.exec(userId, playlistMusicId);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
