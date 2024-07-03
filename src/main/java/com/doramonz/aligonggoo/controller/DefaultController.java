package com.doramonz.aligonggoo.controller;

import com.doramonz.aligonggoo.aop.ViewerCount;
import com.doramonz.aligonggoo.dto.DefaultResponse;
import com.doramonz.aligonggoo.dto.ProductResponse;
import com.doramonz.aligonggoo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DefaultController {

    private final ProductService productService;

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/index";
    }

    @ViewerCount
    @GetMapping("/index")
    public String index(Pageable pageable, Model model) {
        DefaultResponse<ProductResponse> response = productService.getRecentProduct(pageable);
        model.addAttribute("products", response.getData());
        model.addAttribute("total", response.getTotal());
        model.addAttribute("size", response.getSize());
        model.addAttribute("page", response.getPage());
        return "index";
    }
}
