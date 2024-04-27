package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.User;

import java.util.Optional;

public interface UserDataProvider {
    User register(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);
}
