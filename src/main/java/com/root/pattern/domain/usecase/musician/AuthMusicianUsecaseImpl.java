package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.AuthMusicianUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@AllArgsConstructor
@Builder
public class AuthMusicianUsecaseImpl implements AuthMusicianUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MusicianOutputDTO exec(Musician musician) {
        this.validateInputs(musician);
        Musician getMusician = this.checkIfMusicianExists(musician.getEmail());

        this.checkIfPasswordsMatches(musician.getPassword(), getMusician.getPassword());

        return this.mountOutput(getMusician);
    }

    @Override
    public void validateInputs(Musician musician) {
        if (Objects.isNull(musician)) {
            throw new BadRequestException("Musician can't be empty");
        }

        if (Objects.isNull(musician.getEmail())) {
            throw new BadRequestException("Musician e-mail can't be empty");
        }

        if (Objects.isNull(musician.getPassword())) {
            throw new BadRequestException("Musician password can't be empty");
        }
    }

    @Override
    public Musician checkIfMusicianExists(String email) {
        return this.musicianDataProvider.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Musician"));
    }

    @Override
    public void checkIfPasswordsMatches(String raw, String hashed) {
        if (!this.passwordEncoder.matches(raw, hashed)) {
            throw new ForbiddenException("Wrong credentials");
        }
    }

    @Override
    public MusicianOutputDTO mountOutput(Musician musician) {
        return MusicianOutputDTO.builder()
            .id(musician.getId())
            .role(musician.getRole())
            .name(musician.getName())
            .email(musician.getEmail())
            .createdAt(musician.getCreatedAt())
            .build();
    }
}
