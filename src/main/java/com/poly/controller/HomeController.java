package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // Trỏ đến fragment trong layouts/index.html
        model.addAttribute("content", "layouts/index :: content");
        return "layout";  // trả về layout.html
    }
}
