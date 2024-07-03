package com.doramonz.aligonggoo.controller;

import com.doramonz.aligonggoo.dto.DefaultError;
import com.doramonz.aligonggoo.dto.DefaultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(DefaultError.class)
    public ResponseEntity<DefaultResponse> handleDefaultError(DefaultError e) {
        return ResponseEntity.status(e.getStatus()).body(DefaultResponse.builder()
                .message(e.getMessage())
                .status(e.getStatus())
                .build());
    }
}
