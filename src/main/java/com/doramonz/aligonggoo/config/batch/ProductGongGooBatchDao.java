package com.doramonz.aligonggoo.config.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductGongGooBatchDao {
    private int id;
    private String url;
    private boolean status;
    private LocalDateTime created;
}
