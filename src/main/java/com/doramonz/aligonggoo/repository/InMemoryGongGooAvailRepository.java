package com.doramonz.aligonggoo.repository;

import com.doramonz.aligonggoo.dao.ProductGongGooDao;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository("gongGooAvailRepository")
public class InMemoryGongGooAvailRepository implements GongGooAvailRepository {

    private static final int MAX_AVAIL_COUNT = 2;
    private static final long MAX_AVAIL_TIME = 3;

    private final Map<Integer, Map<String, CopyOnWriteArrayList<LocalDateTime>>> gongGooMap = new HashMap<>();

    @Override
    public String getAvailUrl(int productId, List<ProductGongGooDao> gongGooList) {
        String url;
        Map<String, CopyOnWriteArrayList<LocalDateTime>> productGongGooMap = gongGooMap.computeIfAbsent(productId, k -> new HashMap<>());
        url = gongGooList.stream().filter(productGongGooDao -> {
            CopyOnWriteArrayList<LocalDateTime> availList = productGongGooMap.computeIfAbsent(productGongGooDao.getUrl(), k -> new CopyOnWriteArrayList<>());
            availList.forEach(localDateTime -> {
                if(localDateTime.plusHours(MAX_AVAIL_TIME).isBefore(LocalDateTime.now())){
                    availList.remove(localDateTime);
                }
            });
            if(availList.size() < MAX_AVAIL_COUNT){
                availList.add(LocalDateTime.now());
                return true;
            }
            return false;
        }).findFirst().map(ProductGongGooDao::getUrl).orElse(null);
        return url;
    }


}
