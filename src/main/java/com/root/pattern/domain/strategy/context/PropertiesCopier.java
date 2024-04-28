package com.root.pattern.domain.strategy.context;

import com.root.pattern.domain.strategy.CopyPropertiesStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Data
@Builder
public class PropertiesCopier {
    private CopyPropertiesStrategy copyPropertiesStrategy;

    public void copyNonNullProps(Object source, Object target) {
        this.copyPropertiesStrategy.copyNonNullProps(source, target);
    }
}
