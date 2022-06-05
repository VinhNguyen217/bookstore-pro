package com.bookstore.controller.web;

import com.bookstore.model.Book;
import com.bookstore.model.Comment;
import com.bookstore.model.Promotion;
import com.bookstore.service.*;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Utility utility;

    @Autowired
    private UserService userService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PromotionService promotionService;

    @GetMapping({"/", ""})
    public String showHome(Model model) {
        model.addAttribute("bookService", bookService);
        model.addAttribute("authorService", authorService);
        model.addAttribute("utility", utility);
        model.addAttribute("favouriteService", favouriteService);
        model.addAttribute("commentService", commentService);
        return "web/index";
    }

    @GetMapping("/book/{id}")
    public String detailBook(@PathVariable Integer id, Model model) {
        Book book = bookService.getById(id);
        List<Book> bookSimilar = bookService.getSimilar(id);
        List<Comment> commentList = commentService.getByBookId(id);
        if (book.getPromotionId() != null) {
            Promotion promotion = promotionService.getById(book.getPromotionId());
            Integer pricePromotion = bookService.calculatePromotionalMoney(book);
            model.addAttribute("promotion", promotion);
            model.addAttribute("pricePromotion", pricePromotion);
        } else {
            model.addAttribute("promotion", null);
        }
        model.addAttribute("book", book);
        model.addAttribute("bookSimilar", bookSimilar);
        model.addAttribute("commentList", commentList);

        model.addAttribute("authorService", authorService);
        model.addAttribute("categoryService", categoryService);
        model.addAttribute("userService", userService);
        model.addAttribute("utility", utility);
        model.addAttribute("favouriteService", favouriteService);
        model.addAttribute("commentService", commentService);

        return "web/detail";
    }
}
