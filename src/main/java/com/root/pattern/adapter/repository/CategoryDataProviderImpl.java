package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.interfaces.repository.CategoryDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Component
public class CategoryDataProviderImpl implements CategoryDataProvider {
    private final CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findById(UUID id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public void saveAll(List<Category> categories) {
        this.categoryRepository.saveAll(categories);
    }

    @Override
    public Long countAll() {
        return this.categoryRepository.count();
    }

    @Override
    public void deleteAll() {
        this.categoryRepository.deleteAll();
    }
}
