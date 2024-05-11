package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.strategy.context.MailValidator;
import com.root.pattern.domain.strategy.context.PropertiesCopier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Builder
public class UpdateMusicianProfileUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailValidator mailValidator;
    private final PropertiesCopier propertiesCopier;

    public MusicianOutputDTO exec(Musician musician) {
        this.validateInputs(musician);

        if (Objects.nonNull(musician.getEmail())) {
            this.validateEmail(musician.getEmail());

            this.checkIfEmailAlreadyExists(musician.getEmail(), musician);
        }

        Musician getMusician = this.checkIfMusicianExists(musician.getId());

        if (Objects.nonNull(getMusician.getDisabledAt())) {
            throw new ForbiddenException("Musician disabled");
        }

        if (Objects.nonNull(musician.getPassword())) {
            musician.setPassword(this.hashNewPassword(musician.getPassword()));
        }

        this.propertiesCopier.copyNonNullProps(musician, getMusician);

        Musician updateMusician = this.musicianDataProvider.update(getMusician);

        return this.mountOutput(updateMusician);
    }

    public void validateInputs(Musician musician) {
        if (Objects.nonNull(musician.getPassword()) && musician.getPassword().length() < 6) {
            throw new BadRequestException("Password must have at least 6 characters");
        }
    }

    public void validateEmail(String email) {
        if (!this.mailValidator.validate(email)) {
            throw new BadRequestException("Invalid e-mail");
        }
    }

    public void checkIfEmailAlreadyExists(String newEmail, Musician musician) {
        Optional<Musician> findSimilarEmail = this.musicianDataProvider.findByEmail(newEmail);

        if (findSimilarEmail.isPresent()) {
            boolean isFoundMusicianMyself = findSimilarEmail.get().getId().equals(musician.getId());

            if (isFoundMusicianMyself) return;

            throw new ConflictException("E-mail already exists");
        }
    }

    public Musician checkIfMusicianExists(Long id) {
        return this.musicianDataProvider.findById(id)
            .orElseThrow(() -> new NotFoundException("Musician"));
    }

    public String hashNewPassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    public MusicianOutputDTO mountOutput(Musician musician) {
        return MusicianOutputDTO.builder()
            .id(musician.getId())
            .name(musician.getName())
            .email(musician.getEmail())
            .role(musician.getRole())
            .createdAt(musician.getCreatedAt())
            .build();
    }
}
