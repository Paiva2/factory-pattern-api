package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.GetMusicianProfileUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;

@AllArgsConstructor
@Builder
public class GetMusicianProfileUsecaseImpl implements GetMusicianProfileUsecase {
    private final MusicianDataProvider musicianDataProvider;

    @Override
    public MusicianOutputDTO exec(Long id) {
        this.validateInput(id);
        Musician getMusician = this.checkIfMusicianExists(id);

        return this.mountOutput(getMusician);
    }

    @Override
    public void validateInput(Long id) {
        if (Objects.isNull(id)) {
            throw new BadRequestException("Musician id can't be empty");
        }
    }

    @Override
    public Musician checkIfMusicianExists(Long id) {
        return this.musicianDataProvider.findById(id)
            .orElseThrow(() -> new NotFoundException("Musician"));
    }

    @Override
    public MusicianOutputDTO mountOutput(Musician musician) {
        return MusicianOutputDTO.builder()
            .id(musician.getId())
            .name(musician.getName())
            .email(musician.getEmail())
            .createdAt(musician.getCreatedAt())
            .role(musician.getRole())
            .build();
    }
}
