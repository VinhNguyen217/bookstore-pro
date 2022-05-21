package com.bookstore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class HomeController {

    @GetMapping({"/", ""})
    public String showHome(HttpSession session) {
        if (session.getAttribute("admin") != null)
            return "admin/index";
        return "redirect:/admin/login";
    }

}
