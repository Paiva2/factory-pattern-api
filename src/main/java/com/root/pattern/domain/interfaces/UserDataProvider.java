package com.root.pattern.domain.interfaces;

import com.root.pattern.domain.entity.User;

import java.util.Optional;

public interface UserDataProvider {
    User register(User user);

    Optional<User> userExists(String email);
}
