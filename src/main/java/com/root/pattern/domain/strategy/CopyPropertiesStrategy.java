package com.root.pattern.domain.strategy;

public interface CopyPropertiesStrategy {
    void copyNonNullProps(Object source, Object target);
}
