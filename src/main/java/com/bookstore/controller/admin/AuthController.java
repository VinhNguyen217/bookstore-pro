package com.bookstore.controller.admin;

import com.bookstore.security.SessionAdmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AuthController {

    @GetMapping("/login")
    public String showFormLogin() {
        if (!SessionAdmin.getInstance().userList.isEmpty())
            return "redirect:/admin";
        else
            return "admin/auth/login";
    }
}
