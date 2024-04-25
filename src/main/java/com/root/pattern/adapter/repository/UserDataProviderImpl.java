package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.UserDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class UserDataProviderImpl implements UserDataProvider {
    private final UserDataRepository userDataRepository;

    @Override
    public User register(User user) {
        return this.userDataRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userDataRepository.findByEmail(email);
    }
}
