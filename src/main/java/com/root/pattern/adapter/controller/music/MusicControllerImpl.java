package com.root.pattern.adapter.controller.music;

import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.adapter.dto.music.NewMusicDTO;
import com.root.pattern.adapter.dto.music.NewMusicOutputDTO;
import com.root.pattern.domain.interfaces.usecase.FilterMusicsNameUsecase;
import com.root.pattern.domain.interfaces.usecase.RegisterMusicUsecase;
import lombok.AllArgsConstructor;
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
public class MusicControllerImpl implements MusicController {
    private final RegisterMusicUsecase registerMusicUsecase;
    private final FilterMusicsNameUsecase filterMusicsNameUsecase;

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

    @Override
    public ResponseEntity<ListFilterMusicOutputDTO> getMusicByName(
        @RequestParam("name") String name,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer size
    ) {
        ListFilterMusicOutputDTO output = this.filterMusicsNameUsecase.exec(name, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
