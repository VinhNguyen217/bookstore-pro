package com.bookstore.controller.web;

import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.service.*;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebCategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private Utility utility;

    @GetMapping(value = "/category/all")
    public String showAllBooksAndCategories(@RequestParam Integer page, Model model) {
        Integer numberOfPages = utility.getNumberOfPages(bookService.getAllDescId().size());
        List<Category> categoryList = categoryService.getAllDescId();
        List<Book> bookList = bookService.findAllAndPaging(page - 1);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("bookList", bookList);
        model.addAttribute("authorService", authorService);
        model.addAttribute("numberOfPages", numberOfPages);
        model.addAttribute("page", page);   //trang hiện tại
        model.addAttribute("utility", utility);
        model.addAttribute("favouriteService", favouriteService);
        model.addAttribute("commentService", commentService);
        return "web/categories_books";
    }


    @GetMapping("/category/{catId}")
    public String showBooksByCategoryAndPaging(@PathVariable("catId") Integer catId,
                                               @RequestParam("page") Integer page,
                                               Model model) {
        Integer numberOfPages = utility.getNumberOfPages(bookService.getByCategory(catId).size());
        List<Category> categoryList = categoryService.getAllDescId();
        List<Book> bookList = bookService.findByCategoryAndPaging(catId, page - 1);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoryService", categoryService);
        model.addAttribute("bookList", bookList);
        model.addAttribute("authorService", authorService);
        model.addAttribute("numberOfPages", numberOfPages);
        model.addAttribute("cid", catId);
        model.addAttribute("page", page);   //trang hiện tại
        model.addAttribute("utility", utility);
        model.addAttribute("favouriteService", favouriteService);
        model.addAttribute("commentService", commentService);
        return "web/booksByCategory";
    }
}
