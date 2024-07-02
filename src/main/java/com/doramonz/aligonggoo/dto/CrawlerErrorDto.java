package com.doramonz.aligonggoo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
public class CrawlerErrorDto {

    public enum ErrorType {
        RESTART, NOT_AVAILABLE, LOG
    }

    private String instanceId;

    private ErrorType errorType;
    private String message;
    private String url;
    private String stackTrace;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
