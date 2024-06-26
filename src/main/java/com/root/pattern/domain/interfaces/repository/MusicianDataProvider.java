package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Musician;

import java.util.Optional;

public interface MusicianDataProvider {
    Optional<Musician> findById(Long id);

    Optional<Musician> findByEmail(String email);

    Musician register(Musician newMusician);

    Musician update(Musician musician);

    Optional<Musician> findByEmailOrName(String email, String name);

    Optional<Musician> findByName(String name);
}
