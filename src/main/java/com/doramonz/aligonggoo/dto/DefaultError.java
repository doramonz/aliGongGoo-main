package com.doramonz.aligonggoo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DefaultError extends Exception {

    private final String message;
    private final int status;

    public DefaultResponse toResponse() {
        return DefaultResponse.builder()
                .message(message)
                .status(status)
                .build();
    }
}
