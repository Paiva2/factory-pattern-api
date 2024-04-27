package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Musician;

import java.util.Optional;

public interface MusicianDataProvider {
    Optional<Musician> findById(Long id);

    Musician register(Musician newMusician);

    Optional<Musician> findByEmailOrName(String email, String name);
}
