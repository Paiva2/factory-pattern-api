package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryDataProvider {
    Optional<Category> findById(UUID id);
}
