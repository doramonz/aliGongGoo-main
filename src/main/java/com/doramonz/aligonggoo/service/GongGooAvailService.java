package com.doramonz.aligonggoo.service;

import com.doramonz.aligonggoo.dao.ProductGongGooDao;
import com.doramonz.aligonggoo.dto.URLParsingDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
@Repository("gongGooAvailRepository")
public class GongGooAvailService {

    private static final int MAX_AVAIL_COUNT = 1;
    private static final long MAX_AVAIL_TIME = 3;

    private final Map<Integer, Map<String, CopyOnWriteArrayList<LocalDateTime>>> gongGooMap = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> mqMap = new ConcurrentHashMap<>();

    private final MQService mqService;

    private Thread mqThread;

    @PostConstruct
    public void init() {
        mqThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10 * 1000);
                    mqMap.forEach((k, v) -> {
                        if (v.plusMinutes(MAX_AVAIL_TIME).isBefore(LocalDateTime.now())) {
                            mqService.processURL(new URLParsingDto(k));
                            mqMap.remove(k);
                        }
                    });
                } catch (InterruptedException e) {
                    log.error("MQ Thread interrupted", e);
                }
            }
        });
        mqThread.start();
    }

    public String getAvailUrl(int productId, List<ProductGongGooDao> gongGooList) {
        String url;
        Map<String, CopyOnWriteArrayList<LocalDateTime>> productGongGooMap = gongGooMap.computeIfAbsent(productId, k -> new ConcurrentHashMap<>());
        url = gongGooList.stream().filter(productGongGooDao -> {
            CopyOnWriteArrayList<LocalDateTime> availList = productGongGooMap.computeIfAbsent(productGongGooDao.getUrl(), k -> new CopyOnWriteArrayList<>());
            if (availList.size() < MAX_AVAIL_COUNT) {
                availList.add(LocalDateTime.now());
                mqMap.put(productGongGooDao.getUrl(), LocalDateTime.now());
                return true;
            }
            return false;
        }).findFirst().map(ProductGongGooDao::getUrl).orElse(null);
        return url;
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 5 * 60 * 1000)
    public void clearExpiredAvail() {
        gongGooMap.forEach((k, v) -> {
            v.forEach((k2, v2) -> v2.forEach(localDateTime -> {
                if (localDateTime.plusMinutes(MAX_AVAIL_TIME).isBefore(LocalDateTime.now())) {
                    log.debug("Remove expired avail. productId: {}, url: {}", k, k2);
                    v2.remove(localDateTime);
                } else if (!mqMap.containsKey(k2)) {
                    mqService.processURL(new URLParsingDto(k2));
                }
            }));
            if (v.isEmpty()) {
                log.debug("Remove expired product. productId: {}", k);
                gongGooMap.remove(k);
            }
        });
    }

    public void removeAvailAndUrl(String url) {
        mqMap.remove(url);
        gongGooMap.forEach((k, v) -> v.remove(url));
    }


}
