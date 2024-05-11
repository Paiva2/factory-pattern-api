package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDataProvider {
    Optional<Category> findById(UUID id);

    void saveAll(List<Category> categories);

    Long countAll();

    void deleteAll();
}
