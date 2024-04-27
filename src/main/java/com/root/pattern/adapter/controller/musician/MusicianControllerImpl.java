package com.root.pattern.adapter.controller.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.dto.musician.RegisterMusicianDTO;
import com.root.pattern.domain.usecase.musician.RegisterMusicianUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class MusicianControllerImpl implements MusicianController {
    private final RegisterMusicianUsecaseImpl registerMusicianUsecase;

    @Override
    public ResponseEntity<MusicianOutputDTO> register(@RequestBody @Valid RegisterMusicianDTO dto) {
        MusicianOutputDTO output = this.registerMusicianUsecase.exec(dto.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
