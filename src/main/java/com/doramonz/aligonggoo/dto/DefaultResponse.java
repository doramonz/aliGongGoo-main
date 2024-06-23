package com.doramonz.aligonggoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DefaultResponse<T> {
    private List<T> data;
    private String message;
    private Integer status;
    private Integer size;
    private Integer page;
    private Long total;
}
