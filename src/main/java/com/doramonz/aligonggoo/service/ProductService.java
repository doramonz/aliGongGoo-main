package com.doramonz.aligonggoo.service;

import com.doramonz.aligonggoo.dto.*;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    DefaultResponse<ProductResponse> searchProduct(String name, Pageable pageable);

    DefaultResponse<Void> uploadGongGooURL(String userId, String url) throws DefaultError;

    void uploadProduct(AliProductInfo productInfo);

    DefaultResponse<UrlResponse> getGongGooUrl(int productId) throws DefaultError;

    DefaultResponse<Void> deleteGongGoo(int productGongGooId) throws DefaultError;

    DefaultResponse<ProductResponse> getRecentProduct(Pageable pageable);

    void closeGongGoo(String url);
}
