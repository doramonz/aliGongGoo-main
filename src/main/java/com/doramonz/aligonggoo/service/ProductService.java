package com.doramonz.aligonggoo.service;

import com.doramonz.aligonggoo.dto.DefaultError;
import com.doramonz.aligonggoo.dto.DefaultResponse;
import com.doramonz.aligonggoo.dto.ProductResponse;
import com.doramonz.aligonggoo.dto.UrlResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    DefaultResponse<ProductResponse> searchProduct(String name, Pageable pageable);

    DefaultResponse<Void> uploadProduct(String userId, String url) throws DefaultError;

    DefaultResponse<UrlResponse> getGongGooUrl(int productId) throws DefaultError;
}
