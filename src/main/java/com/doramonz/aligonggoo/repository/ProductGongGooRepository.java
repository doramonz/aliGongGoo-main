package com.doramonz.aligonggoo.repository;

import com.doramonz.aligonggoo.dao.ProductGongGooDao;
import com.doramonz.aligonggoo.domain.ProductGongGoo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductGongGooRepository extends JpaRepository<ProductGongGoo, Integer> {

    Optional<ProductGongGoo> findByUrl(String url);

    @Query("select new com.doramonz.aligonggoo.dao.ProductGongGooDao(pg.id, pg.url, pg.created) from ProductGongGoo pg where pg.product.id = :productId and pg.status = true")
    List<ProductGongGooDao> getOpenedProductGongGooList(int productId);

    @Modifying
    @Query("update ProductGongGoo pgg set pgg.status = :b where pgg.id = :id")
    void updateStatus(int id, boolean b);
}
