package com.doramonz.aligonggoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private final int id;
    private final String name;
    private final String image;
}
