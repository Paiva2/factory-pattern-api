package com.root.pattern.adapter.controller.playlist;

import com.root.pattern.adapter.dto.playlist.ListOwnPlaylistsOutputdTO;
import com.root.pattern.adapter.dto.playlist.NewPlaylistInputDTO;
import com.root.pattern.adapter.dto.playlist.NewPlaylistOutputDTO;
import com.root.pattern.domain.interfaces.usecase.CreateNewPlaylistUsecase;
import com.root.pattern.domain.interfaces.usecase.ListOwnPlaylistsUsecase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class PlaylistControllerImpl implements PlaylistController {
    private final CreateNewPlaylistUsecase createNewPlaylistUsecase;
    private final ListOwnPlaylistsUsecase listOwnPlaylistsUsecase;

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
    public ResponseEntity<ListOwnPlaylistsOutputdTO> listOwn(
        Authentication authentication,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer perPage,
        @RequestParam(value = "name", required = false) String name
    ) {
        Long userId = Long.valueOf(authentication.getName());
        ListOwnPlaylistsOutputdTO output = this.listOwnPlaylistsUsecase.exec(userId, page, perPage, name);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
