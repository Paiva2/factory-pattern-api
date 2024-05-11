package com.root.pattern.application.config;

import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.enums.MusicCategory;
import com.root.pattern.domain.interfaces.repository.CategoryDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class InitSetup implements CommandLineRunner {
    private final CategoryDataProvider categoryDataProvider;

    @Override
    public void run(String... args) {
        Long categoriesCount = this.categoryDataProvider.countAll();

        if (categoriesCount > 11) return;

        this.createCategories();
    }

    private void createCategories() {
        this.categoryDataProvider.deleteAll();

        List<Category> categories = new ArrayList<>();

        for (MusicCategory category : MusicCategory.values()) {
            categories.add(Category.builder()
                .name(category)
                .disabled(false)
                .build());
        }

        this.categoryDataProvider.saveAll(categories);
    }
}
