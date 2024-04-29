package com.root.pattern.adapter.controller.musician;

import com.root.pattern.adapter.dto.musician.*;
import com.root.pattern.adapter.utils.JwtHandler;
import com.root.pattern.domain.usecase.musician.AuthMusicianUsecaseImpl;
import com.root.pattern.domain.usecase.musician.GetMusicianProfileUsecaseImpl;
import com.root.pattern.domain.usecase.musician.RegisterMusicianUsecaseImpl;
import com.root.pattern.domain.usecase.musician.UpdateMusicianProfileUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class MusicianControllerImpl implements MusicianController {
    private final RegisterMusicianUsecaseImpl registerMusicianUsecase;
    private final GetMusicianProfileUsecaseImpl getMusicianProfileUsecase;
    private final AuthMusicianUsecaseImpl authMusicianUsecase;
    private final UpdateMusicianProfileUsecaseImpl updateMusicianProfileUsecase;

    private final JwtHandler jwtHandler;

    @Override
    public ResponseEntity<MusicianOutputDTO> register(@RequestBody @Valid RegisterMusicianDTO dto) {
        MusicianOutputDTO output = this.registerMusicianUsecase.exec(dto.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @Override
    public ResponseEntity<MusicianOutputDTO> profile(Authentication authentication) {
        MusicianOutputDTO output = this.getMusicianProfileUsecase.exec(
            Long.valueOf(authentication.getName())
        );

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<AuthMusicianDTO> login(
        @RequestBody
        @Valid LoginMusicianDTO dto
    ) {
        MusicianOutputDTO output = this.authMusicianUsecase.exec(dto.toEntity());
        String token = this.jwtHandler.generate(output.getId().toString(), output.getRole());

        return ResponseEntity.status(HttpStatus.OK).body(
            AuthMusicianDTO.builder()
                .email(output.getEmail())
                .authToken(token)
                .build()
        );
    }

    @Override
    public ResponseEntity<MusicianOutputDTO> updateProfile(
        Authentication authentication,
        @RequestBody @Valid UpdateMusicianProfileDTO dto
    ) {
        Long userId = Long.valueOf(authentication.getName());
        MusicianOutputDTO output = this.updateMusicianProfileUsecase.exec(dto.toEntity(userId));

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
