package com.bookstore.controller.web;

import com.bookstore.response.ResponseMessage;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WebAuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegister() {
        return "web/register";
    }

    @GetMapping("/register/verify")
    public String verifyCode(@RequestParam("c") String verifyCode, Model model) {
        boolean verify = userService.verify(verifyCode);
        model.addAttribute("verify", verify);
        if (verify) {
            model.addAttribute("title_success", ResponseMessage.VERIFY_TITLE_SUCCESS);
            model.addAttribute("msg_success", ResponseMessage.VERIFY_USER_SUCCESS);
        } else
            model.addAttribute("title_error", ResponseMessage.VERIFY_TITLE_ERROR);
        return "web/verify";
    }

    @GetMapping("/login")
    public String login() {
        return "web/login";
    }

}
