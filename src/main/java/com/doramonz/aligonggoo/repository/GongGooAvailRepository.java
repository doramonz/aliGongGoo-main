package com.doramonz.aligonggoo.repository;

import com.doramonz.aligonggoo.dao.ProductGongGooDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GongGooAvailRepository {
    String getAvailUrl(int productId, List<ProductGongGooDao> gongGooList);
}
