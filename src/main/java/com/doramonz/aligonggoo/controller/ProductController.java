package com.doramonz.aligonggoo.controller;

import com.doramonz.aligonggoo.dto.DefaultError;
import com.doramonz.aligonggoo.dto.DefaultResponse;
import com.doramonz.aligonggoo.dto.ProductResponse;
import com.doramonz.aligonggoo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.UriEncoder;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/search")
    public DefaultResponse<ProductResponse> searchProduct(@RequestParam("name") String name, Pageable pageable) {
        return productService.searchProduct(name, pageable);
    }

    @PostMapping("/upload")
    public DefaultResponse uploadProduct(@RequestParam("url") String url) throws DefaultError {
        return productService.uploadProduct(SecurityContextHolder.getContext().getAuthentication().getName(), UriEncoder.decode(url));
    }

    @GetMapping("/opened")
    public DefaultResponse getOpenedProductGongGooList(@RequestParam("productId") int productId) throws DefaultError {
        return productService.getGongGooUrl(productId);
    }
}
