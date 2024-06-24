package com.doramonz.aligonggoo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Aspect
@Component
public class ViewerCounter {

    private final static AtomicInteger viewerCount = new AtomicInteger(0);

    @Pointcut("@annotation(com.doramonz.aligonggoo.aop.ViewerCount)")
    public void viewerCount() {
    }

    @AfterReturning("viewerCount()")
    public void increaseViewerCount() {
        viewerCount.incrementAndGet();
    }

    @Scheduled(cron = "23 59 00 * * *")
    public void resetViewerCount() {
        log.info("Today Total viewerCount: {}, Time: {}", viewerCount.get(), LocalDateTime.now().withNano(0));
        viewerCount.set(0);
    }
}
