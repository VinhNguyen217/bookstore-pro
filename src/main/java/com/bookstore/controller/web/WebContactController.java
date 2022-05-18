package com.bookstore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebContactController {

    @GetMapping("/contact")
    public String showContact() {
        return "web/contact";
    }
}
