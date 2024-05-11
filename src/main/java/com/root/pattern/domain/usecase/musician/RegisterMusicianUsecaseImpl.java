package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.musician.RegisterMusicianUsecase;
import com.root.pattern.domain.strategy.context.MailValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@Builder
@AllArgsConstructor
public class RegisterMusicianUsecaseImpl implements RegisterMusicianUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailValidator mailValidator;

    public MusicianOutputDTO exec(Musician newMusician) {
        this.inputValidations(newMusician);
        this.checkIfUserAlreadyExists(newMusician.getEmail(), newMusician.getName());

        String hashedPassword = this.hashPassword(newMusician.getPassword());

        newMusician.setPassword(hashedPassword);
        newMusician.setRole(Role.MUSICIAN);

        Musician registeredMusician = this.musicianDataProvider.register(newMusician);

        return this.mountDto(registeredMusician);
    }

    public void inputValidations(Musician newMusician) {
        if (Objects.isNull(newMusician)) {
            throw new BadRequestException("Musician can't be null");
        }

        if (Objects.isNull(newMusician.getEmail())) {
            throw new BadRequestException("Musician e-mail can't be empty");
        }

        if (Objects.isNull(newMusician.getName())) {
            throw new BadRequestException("Musician name can't be empty");
        }

        if (newMusician.getPassword().length() < 6) {
            throw new BadRequestException("Password must have at least 6 characters");
        }

        if (!this.mailValidator.validate(newMusician.getEmail())) {
            throw new BadRequestException("Invalid e-mail");
        }
    }

    public void checkIfUserAlreadyExists(String musicianEmail, String musicianName) {
        Musician musician = this.musicianDataProvider.findByEmailOrName(musicianEmail, musicianName).orElse(null);

        if (Objects.nonNull(musician)) {
            throw new ConflictException("Musician already exists");
        }
    }

    public String hashPassword(String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    public MusicianOutputDTO mountDto(Musician newMusician) {
        return MusicianOutputDTO.builder()
            .id(newMusician.getId())
            .name(newMusician.getName())
            .email(newMusician.getEmail())
            .role(newMusician.getRole())
            .createdAt(newMusician.getCreatedAt())
            .build();
    }
}
