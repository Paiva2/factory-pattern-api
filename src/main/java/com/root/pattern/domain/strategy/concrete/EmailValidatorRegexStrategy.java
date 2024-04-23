package com.root.pattern.domain.strategy.concrete;

import com.root.pattern.domain.strategy.EmailValidatorStrategy;

import java.util.regex.Pattern;

public class EmailValidatorRegexStrategy implements EmailValidatorStrategy {
    private final static String MAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    @Override
    public boolean validate(String email) {
        return Pattern.compile(MAIL_REGEX, Pattern.CASE_INSENSITIVE).matcher(email).matches();
    }
}
