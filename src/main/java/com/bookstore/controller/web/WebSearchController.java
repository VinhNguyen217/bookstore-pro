package com.bookstore.controller.web;

import com.bookstore.model.Book;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import com.bookstore.service.FavouriteService;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
public class WebSearchController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private Utility utility;

    @Autowired
    private FavouriteService favouriteService;

    @GetMapping("")
    public String searchByName(@RequestParam("q") String name,
                               Model model) {
        List<Book> bookList = bookService.findByName(name);
        model.addAttribute("name",name);
        model.addAttribute("bookList", bookList);
        model.addAttribute("authorService", authorService);
        model.addAttribute("utility", utility);
        model.addAttribute("favouriteService",favouriteService);
        return "web/search";
    }
}
