package com.bookstore.controller.web;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
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
public class WebAuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private Utility utility;

    @GetMapping("/author/all")
    public String showAllAuthors(@RequestParam Integer page, Model model) {
        Integer numberOfPages = utility.getNumberAuthorOfPages(authorService.getAllDescId().size());
        List<Author> authorList = authorService.findAllAndPaging(page - 1);
        if (page <= 0) page = 1;
        model.addAttribute("authorList", authorList);
        model.addAttribute("numberOfPages", numberOfPages);
        model.addAttribute("page", page);   //trang hiện tại
        return "web/authors";
    }

    @GetMapping("/author/{id}")
    public String showBooksByAuthor(@PathVariable("id") Integer authorId, Model model) {
        Author author = authorService.getById(authorId);
        List<Book> bookList = bookService.getByAuthor(authorId);

        model.addAttribute("author", author);
        model.addAttribute("bookList", bookList);
        model.addAttribute("authorService", authorService);
        model.addAttribute("utility", utility);
        model.addAttribute("favouriteService", favouriteService);
        model.addAttribute("commentService", commentService);
        return "web/authorDetail";
    }
}
