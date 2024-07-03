package com.doramonz.aligonggoo.service;

import com.doramonz.aligonggoo.dao.ProductGongGooDao;
import com.doramonz.aligonggoo.domain.Product;
import com.doramonz.aligonggoo.domain.ProductGongGoo;
import com.doramonz.aligonggoo.dto.*;
import com.doramonz.aligonggoo.repository.ProductGongGooRepository;
import com.doramonz.aligonggoo.repository.ProductRepository;
import com.doramonz.aligonggoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
@Service("productService")
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;
    private final ProductGongGooRepository productGongGooRepository;
    private final UserRepository userRepository;
    private final GongGooAvailService gongGooAvailService;
    private final MQService mqService;

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
    public DefaultResponse<Void> uploadGongGooURL(String userId, String url) throws DefaultError {

        if (!(url.startsWith("https://www.aliexpress.com") || url.startsWith("https://a.aliexpress.com"))) {
            throw new DefaultError("올바르지 않은 URL입니다.", 400);
        }
        DefaultResponse<Void> response;

        Optional<ProductGongGoo> findGongGoo = productGongGooRepository.findByUrl(url);
        if (findGongGoo.isPresent()) {
            throw new DefaultError("이미 등록된 공구입니다.", 400);
        }

        productGongGooRepository.save(ProductGongGoo.builder()
                .user(userRepository.getReferenceById(userId))
                .url(url)
                .status(true)
                .created(LocalDateTime.now().withNano(0))
                .build());

        mqService.processURL(new URLParsingDto(url));

        response = DefaultResponse.<Void>builder()
                .status(200)
                .message("Success")
                .build();

        return response;
    }

    @Override
    @Transactional
    public void uploadProduct(AliProductInfo productInfo) {

        Optional<Product> find = productRepository.findByName(productInfo.getTitle());
        Optional<ProductGongGoo> findGongGoo = productGongGooRepository.findByUrl(productInfo.getUrl());

        if (findGongGoo.isEmpty()) {
            log.debug("공구 정보를 찾을 수 없습니다. {}", productInfo.getUrl());
            return;
        }

        if (find.isEmpty()) {
            Product product = productRepository.save(Product.builder()
                    .name(productInfo.getTitle())
                    .image(productInfo.getImage())
                    .build());
            productGongGooRepository.save(ProductGongGoo.builder()
                    .product(product)
                    .user(userRepository.getReferenceById(findGongGoo.get().getUser().getId()))
                    .url(productInfo.getUrl())
                    .price(productInfo.getPrice())
                    .status(true)
                    .created(LocalDateTime.now().withNano(0))
                    .build());
        } else {
            productGongGooRepository.save(ProductGongGoo.builder()
                    .product(find.get())
                    .user(userRepository.getReferenceById(findGongGoo.get().getUser().getId()))
                    .url(productInfo.getUrl())
                    .price(productInfo.getPrice())
                    .status(true)
                    .created(LocalDateTime.now().withNano(0))
                    .build());
        }
    }

    @Override
    @Transactional
    public DefaultResponse<UrlResponse> getGongGooUrl(int productId) throws DefaultError {
        if (!productRepository.existsById(productId)) {
            throw new DefaultError("상품을 찾을 수 없습니다.", 404);
        }
        List<ProductGongGooDao> gongGooList = productGongGooRepository.getOpenedProductGongGooList(productId);
        CopyOnWriteArrayList<Integer> closedGongGooList = new CopyOnWriteArrayList<>();

        gongGooList.stream().filter(
                gongGoo -> gongGoo.getCreated().plusDays(1).isBefore(LocalDateTime.now())
        ).forEach(gongGoo -> closedGongGooList.add(gongGoo.getId()));

        closedGongGooList.forEach(id -> {
            productGongGooRepository.updateStatus(id, false);
            gongGooList.removeIf(gongGoo -> gongGoo.getId() == id);
        });

        String url = gongGooAvailService.getAvailUrl(productId, gongGooList);

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

    @Transactional
    @Override
    public void closeGongGoo(String url) {
        Optional<ProductGongGoo> productGongGoo = productGongGooRepository.findByUrl(url);
        productGongGoo.ifPresent(gongGoo -> productGongGooRepository.updateStatus(gongGoo.getId(), false));
        gongGooAvailService.removeAvailAndUrl(url);
    }
}
