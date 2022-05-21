package com.bookstore.controller.admin;

import com.bookstore.model.User;
import com.bookstore.response.ResponseMessage;
import com.bookstore.service.UserService;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private Utility utility;

    @GetMapping({"", "/"})
    public String showAccounts(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            List<User> userList = userService.getAll();
            model.addAttribute("accountList", userList);
            return "admin/account/accounts";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/{id}")
    public String showAccountDetail(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            User account = userService.getById(id);
            model.addAttribute("account", account);
            model.addAttribute("utility", utility);
            model.addAttribute("title", "Tài khoản : " + account.getUserName());
            return "admin/account/detail";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Integer id, RedirectAttributes ra, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            userService.delete(id);
            ra.addFlashAttribute("msg_success", ResponseMessage.DELETE_SUCCESS);
            return "redirect:/admin/account";
        }
        return "redirect:/admin/login";
    }
}
