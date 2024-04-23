package com.root.pattern.domain.strategy;

public interface EmailValidatorStrategy {
    boolean validate(String email);
}
