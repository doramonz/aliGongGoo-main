package com.doramonz.aligonggoo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductGongGooDto {
    private int id;
    private String name;
    private String url;
    private LocalDateTime created;
    private boolean status;
}
