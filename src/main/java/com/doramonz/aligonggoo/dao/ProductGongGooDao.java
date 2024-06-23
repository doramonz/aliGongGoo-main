package com.doramonz.aligonggoo.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductGongGooDao {
    private int id;
    private String url;
    private LocalDateTime created;
}
