package com.doramonz.aligonggoo.service;

import com.doramonz.aligonggoo.dao.ProductGongGooDao;
import com.doramonz.aligonggoo.domain.Product;
import com.doramonz.aligonggoo.domain.ProductGongGoo;
import com.doramonz.aligonggoo.dto.DefaultError;
import com.doramonz.aligonggoo.dto.DefaultResponse;
import com.doramonz.aligonggoo.dto.ProductResponse;
import com.doramonz.aligonggoo.dto.UrlResponse;
import com.doramonz.aligonggoo.repository.GongGooAvailRepository;
import com.doramonz.aligonggoo.repository.ProductGongGooRepository;
import com.doramonz.aligonggoo.repository.ProductRepository;
import com.doramonz.aligonggoo.repository.UserRepository;
import com.doramonz.aligonggoo.util.AliProductInfo;
import com.doramonz.aligonggoo.util.AliProductUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@Service("productService")
public class DefaultProductService implements ProductService {

    private final AliProductUtil aliProductUtil;
    private final ProductRepository productRepository;
    private final ProductGongGooRepository productGongGooRepository;
    private final UserRepository userRepository;
    private final GongGooAvailRepository gongGooAvailRepository;

    @Override
    public DefaultResponse<ProductResponse> searchProduct(String name, Pageable pageable) {
        Page<ProductResponse> products = productRepository.findProductsByName(name, pageable);
        return DefaultResponse.<ProductResponse>builder()
                .data(products.getContent())
                .total(products.getTotalElements())
                .size(products.getSize())
                .page(products.getNumber())
                .status(200)
                .message("Success")
                .build();
    }

    @Override
    @Transactional
    public DefaultResponse<Void> uploadProduct(String userId, String url) throws DefaultError {
        AliProductInfo productInfo = aliProductUtil.getProductInfo(url);
        DefaultResponse<Void> response;

        Optional<ProductGongGoo> findGongGoo = productGongGooRepository.findByUrl(url);
        if (findGongGoo.isPresent()) {
            return DefaultResponse.<Void>builder()
                    .status(400)
                    .message("이미 등록된 공구입니다.")
                    .build();
        }

        Optional<Product> find = productRepository.findByName(productInfo.getTitle());

        if (find.isEmpty()) {
            Product product = productRepository.save(Product.builder()
                    .name(productInfo.getTitle())
                    .image(productInfo.getImage())
                    .build());
            productGongGooRepository.save(ProductGongGoo.builder()
                    .product(product)
                    .user(userRepository.getReferenceById(userId))
                    .url(url)
                    .price(productInfo.getPrice())
                    .status(true)
                    .created(LocalDateTime.now().withNano(0))
                    .build());
            response = DefaultResponse.<Void>builder()
                    .status(201)
                    .message("Created")
                    .build();
        } else {
            productGongGooRepository.save(ProductGongGoo.builder()
                    .product(find.get())
                    .user(userRepository.getReferenceById(userId))
                    .url(url)
                    .price(productInfo.getPrice())
                    .status(true)
                    .created(LocalDateTime.now().withNano(0))
                    .build());
            response = DefaultResponse.<Void>builder()
                    .status(200)
                    .message("Success")
                    .build();
        }
        return response;
    }

    @Override
    @Transactional
    public DefaultResponse<UrlResponse> getGongGooUrl(int productId) throws DefaultError {
        if (!productRepository.existsById(productId)) {
            throw new DefaultError("상품을 찾을 수 없습니다.", 404);
        }
        List<ProductGongGooDao> gongGooList = productGongGooRepository.getOpenedProductGongGooList(productId);
        CopyOnWriteArrayList<Integer> closedGongGooList = new CopyOnWriteArrayList<>();

        gongGooList.forEach(gongGoo -> {
            try {
                if (gongGoo.getCreated().plusDays(1).isBefore(LocalDateTime.now())) {
                    throw new DefaultError("공구가 종료되었습니다.", 404);
                }
                aliProductUtil.getProductInfo(gongGoo.getUrl());
            } catch (DefaultError e) {
                closedGongGooList.add(gongGoo.getId());
            }
        });

        for (int id : closedGongGooList) {
            productGongGooRepository.updateStatus(id, false);
            gongGooList.removeIf(gongGoo -> gongGoo.getId() == id);
        }

        String url = gongGooAvailRepository.getAvailUrl(productId, gongGooList);
        if (url == null) {
            throw new DefaultError("공구가 모두 종료되었습니다.", 404);
        }
        return DefaultResponse.<UrlResponse>builder()
                .data(List.of(new UrlResponse(url)))
                .status(200)
                .message("Success")
                .build();
    }

    @Override
    public DefaultResponse<Void> deleteGongGoo(int productGongGooId) throws DefaultError {
        ProductGongGoo productGongGoo = productGongGooRepository.findById(productGongGooId)
                .orElseThrow(() -> new DefaultError("공구를 찾을 수 없습니다.", 404));
        productGongGooRepository.delete(productGongGoo);
        return DefaultResponse.<Void>builder()
                .status(200)
                .message("Success")
                .build();
    }

    @Override
    public DefaultResponse<ProductResponse> getRecentProduct(Pageable pageable) {
        Page<ProductResponse> products = productRepository.getOpenedProductGongGooListRecent(pageable);
        return DefaultResponse.<ProductResponse>builder()
                .data(products.getContent())
                .total(products.getTotalElements())
                .size(products.getSize())
                .page(products.getNumber())
                .status(200)
                .message("Success")
                .build();
    }
}
