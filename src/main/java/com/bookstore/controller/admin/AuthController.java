package com.bookstore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class AuthController {

    @GetMapping("/login")
    public String showFormLogin(HttpSession session) {
        if (session.getAttribute("admin") != null)
            return "redirect:/admin";
        else
            return "/admin/login";
    }
}
