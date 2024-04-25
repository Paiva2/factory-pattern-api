package com.root.pattern.application.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionConfig {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Map<String, Object>> outputExceptionHandler(MethodArgumentNotValidException exception) {

        List<String> errors = exception.getFieldErrors().stream().map(err ->
                MessageFormat.format("{0} {1}", err.getField(), err.getDefaultMessage())
        ).collect(Collectors.toList());

        Map<String, Object> errorMapping = new LinkedHashMap<String, Object>() {{
            put("status", 422);
            put("errors", errors);
        }};

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMapping);
    }
}
