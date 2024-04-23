package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDataRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
