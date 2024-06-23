package com.doramonz.aligonggoo.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public class GongGooAvailInfo {
    private final AtomicInteger count = new AtomicInteger(0);
    private final LocalDateTime created;
}
