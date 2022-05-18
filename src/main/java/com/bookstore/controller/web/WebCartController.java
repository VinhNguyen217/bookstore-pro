package com.bookstore.controller.web;

import com.bookstore.model.CartItem;
import com.bookstore.service.*;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebCartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    @Autowired
    private Utility utility;

    @GetMapping("/cart")
    public String showCart(Model model) {
        List<CartItem> cartLists = cartService.findByUser();
        model.addAttribute("cartLists", cartLists);
        model.addAttribute("bookService", bookService);
        model.addAttribute("utility", utility);
        return "web/cart";
    }

}
