package com.root.pattern.adapter.controller.album;

import com.root.pattern.adapter.dto.album.NewAlbumInputDTO;
import com.root.pattern.adapter.dto.album.NewAlbumOutputDTO;
import com.root.pattern.domain.interfaces.CreateAlbumUsecase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class AlbumControllerImpl implements AlbumController {
    private final CreateAlbumUsecase createAlbumUsecase;

    @Override
    public ResponseEntity<NewAlbumOutputDTO> create(
        Authentication authentication,
        @RequestBody @Valid NewAlbumInputDTO input
    ) {
        NewAlbumOutputDTO output = this.createAlbumUsecase.exec(Long.valueOf(authentication.getName()), input.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
