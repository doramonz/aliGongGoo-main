package com.doramonz.aligonggoo.controller;

import com.doramonz.aligonggoo.dto.DefaultError;
import com.doramonz.aligonggoo.dto.DefaultResponse;
import com.doramonz.aligonggoo.dto.ProductResponse;
import com.doramonz.aligonggoo.dto.UrlResponse;
import com.doramonz.aligonggoo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.UriEncoder;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/search")
    public DefaultResponse<ProductResponse> searchProduct(@RequestParam("name") String name, Pageable pageable) {
        return productService.searchProduct(name, pageable);
    }

    @ResponseBody
    @PostMapping("/upload")
    public DefaultResponse<Void> uploadProduct(@RequestParam("url") String url) throws DefaultError {
        return productService.uploadProduct(SecurityContextHolder.getContext().getAuthentication().getName(), UriEncoder.decode(url));
    }

    @ResponseBody
    @GetMapping("/opened")
    public DefaultResponse<UrlResponse> getOpenedProductGongGooList(@RequestParam("productId") int productId) throws DefaultError {
        return productService.getGongGooUrl(productId);
    }

    @ResponseBody
    @DeleteMapping
    public DefaultResponse<Void> deleteProductGongGoo(@RequestParam("productGongGooId") int productGongGooId) throws DefaultError {
        return productService.deleteGongGoo(productGongGooId);
    }
}
