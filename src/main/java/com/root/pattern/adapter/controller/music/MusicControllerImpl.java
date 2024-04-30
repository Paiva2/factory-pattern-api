package com.root.pattern.adapter.controller.music;

import com.root.pattern.adapter.dto.music.NewMusicDTO;
import com.root.pattern.adapter.dto.music.NewMusicOutputDTO;
import com.root.pattern.domain.usecase.music.RegisterMusicUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class MusicControllerImpl implements MusicController {
    private final RegisterMusicUsecaseImpl registerMusicUsecase;

    @Override
    public ResponseEntity<NewMusicOutputDTO> register(
        @PathVariable(name = "categoryId") UUID categoryId,
        Authentication authentication,
        @RequestBody @Valid NewMusicDTO newMusicDTO
    ) {
        Long musicianId = Long.valueOf(authentication.getName());
        NewMusicOutputDTO output = this.registerMusicUsecase.exec(musicianId, newMusicDTO.toEntity(categoryId));

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
