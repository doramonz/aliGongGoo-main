package com.doramonz.aligonggoo.controller;

import com.doramonz.aligonggoo.dto.DefaultError;
import com.doramonz.aligonggoo.dto.DefaultResponse;
import com.doramonz.aligonggoo.dto.ProductResponse;
import com.doramonz.aligonggoo.dto.UrlResponse;
import com.doramonz.aligonggoo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.UriEncoder;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products/search")
    public String searchProduct(@RequestParam("name") String name, Pageable pageable, Model model) {
        DefaultResponse<ProductResponse> response = productService.searchProduct(name, pageable);
        model.addAttribute("products", response.getData());
        model.addAttribute("total", response.getTotal());
        model.addAttribute("size", response.getSize());
        model.addAttribute("page", response.getPage());
        return "index";
    }

    @GetMapping("/products/upload")
    public String uploadProduct() {
        return "upload";
    }

    @Secured("ROLE_USER")
    @ResponseBody
    @PostMapping("/api/products/upload")
    public DefaultResponse<Void> uploadProduct(@RequestParam("uri") String uri) throws DefaultError {
        return productService.uploadProduct(SecurityContextHolder.getContext().getAuthentication().getName(), UriEncoder.decode(uri));
    }

    @ResponseBody
    @GetMapping("/api/products/opened")
    public DefaultResponse<UrlResponse> getOpenedProductGongGooList(@RequestParam("productId") int productId) throws DefaultError {
        return productService.getGongGooUrl(productId);
    }

    @Secured("ROLE_USER")
    @ResponseBody
    @DeleteMapping("/api/products/delete")
    public DefaultResponse<Void> deleteProductGongGoo(@RequestParam("productGongGooId") int productGongGooId) throws DefaultError {
        return productService.deleteGongGoo(productGongGooId);
    }
}
