package com.restoran.restoransiparisyonetimi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. DOĞRULAMA (VALIDATION) HATALARI İÇİN
    // Boş bırakılamaz denilen alanlar boş gelirse burası çalışır
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException exception) {

        Map<String, String> validationErrors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Validation Error");
        responseBody.put("messages", validationErrors);

        return ResponseEntity.badRequest().body(responseBody);
    }

    // 2. VERİ BULUNAMADI (NOT FOUND) HATALARI İÇİN (Hocanın Tarzı)
    // Veri tabanında yemek veya sipariş bulunamazsa burası çalışır
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResponseStatusException exception) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", exception.getStatusCode().value());
        responseBody.put("error", "Not Found");
        responseBody.put("message", exception.getReason()); // Hata mesajını burası alır

        return ResponseEntity.status(exception.getStatusCode()).body(responseBody);
    }
}