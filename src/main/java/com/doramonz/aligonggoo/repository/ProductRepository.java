package com.doramonz.aligonggoo.repository;

import com.doramonz.aligonggoo.domain.Product;
import com.doramonz.aligonggoo.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT new com.doramonz.aligonggoo.dto.ProductResponse(p.id, p.name,p.image) FROM Product p RIGHT JOIN ProductGongGoo pgg on pgg.product = p WHERE p.name LIKE %:name% AND pgg.status = true GROUP BY p.name ORDER BY pgg.created DESC")
    Page<ProductResponse> findProductsByName(String name, Pageable pageable);

    Optional<Product> findByName(String name);

    @Query("select new com.doramonz.aligonggoo.dto.ProductResponse(p.id, p.name, p.image) from Product p RIGHT join ProductGongGoo pg on p.id = pg.product.id where pg.status = true and pg.product != null group by p.id order by max(pg.created) desc")
    Page<ProductResponse> getOpenedProductGongGooListRecent(Pageable pageable);
}
