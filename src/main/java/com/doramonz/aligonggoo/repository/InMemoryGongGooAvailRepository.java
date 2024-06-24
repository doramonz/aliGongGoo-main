package com.doramonz.aligonggoo.repository;

import com.doramonz.aligonggoo.dao.ProductGongGooDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Repository("gongGooAvailRepository")
public class InMemoryGongGooAvailRepository implements GongGooAvailRepository {

    private static final int MAX_AVAIL_COUNT = 2;
    private static final long MAX_AVAIL_TIME = 1;

    private final Map<Integer, Map<String, CopyOnWriteArrayList<LocalDateTime>>> gongGooMap = new ConcurrentHashMap<>();

    @Override
    public String getAvailUrl(int productId, List<ProductGongGooDao> gongGooList) {
        String url;
        Map<String, CopyOnWriteArrayList<LocalDateTime>> productGongGooMap = gongGooMap.computeIfAbsent(productId, k -> new ConcurrentHashMap<>());
        url = gongGooList.stream().filter(productGongGooDao -> {
            CopyOnWriteArrayList<LocalDateTime> availList = productGongGooMap.computeIfAbsent(productGongGooDao.getUrl(), k -> new CopyOnWriteArrayList<>());
            availList.forEach(localDateTime -> {
                if (localDateTime.plusMinutes(MAX_AVAIL_TIME).isBefore(LocalDateTime.now())) {
                    availList.remove(localDateTime);
                }
            });
            if (availList.size() < MAX_AVAIL_COUNT) {
                availList.add(LocalDateTime.now());
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
                }
            }));
            if (v.isEmpty()) {
                log.debug("Remove expired product. productId: {}", k);
                gongGooMap.remove(k);
            }
        });
    }


}
