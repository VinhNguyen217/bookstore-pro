package com.bookstore.controller.admin;

import com.bookstore.model.Book;
import com.bookstore.response.ResponseMessage;
import com.bookstore.service.*;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/admin/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private Utility utility;

    @GetMapping({"", "/"})
    public String showBooks(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            model.addAttribute("bookList", bookService.getAllDescId());
            model.addAttribute("categoryService", categoryService);
            model.addAttribute("authorService", authorService);
            model.addAttribute("utility", utility);
            return "admin/book/books";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/add")
    public String showFormAdd(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            model.addAttribute("book", new Book());
            model.addAttribute("listCategories", categoryService.getAll());
            model.addAttribute("listAuthors", authorService.getAll());
            model.addAttribute("title", "Thêm Mới");
            return "admin/book/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/{id}")
    public String showBookById(@PathVariable("id") Integer id, Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            Book book = bookService.getById(id);
            model.addAttribute("book", book);
            model.addAttribute("listCategories", categoryService.getAll());
            model.addAttribute("listAuthors", authorService.getAll());
            model.addAttribute("title", "Sách : " + book.getName());
            return "admin/book/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
    public String saveOrUpdate(@ModelAttribute(name = "book") Book book, @RequestParam("fileImage") MultipartFile fileImage, RedirectAttributes ra) throws IOException {
        if (book.getId() == null) {
            ra.addFlashAttribute("msg_success", ResponseMessage.UPDATE_SUCCESS);
            bookService.createBook(book, fileImage);
            return "redirect:/admin/book";
        } else {
            ra.addFlashAttribute("msg_success", ResponseMessage.UPDATE_SUCCESS);
            bookService.updateBook(book, fileImage);
            return "redirect:/admin/book";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            try {
                bookService.delete(id);
                ra.addFlashAttribute("msg_success", ResponseMessage.DELETE_SUCCESS);
            } catch (DataIntegrityViolationException ex) {
                ra.addFlashAttribute("msg_error", ResponseMessage.SQL_ERROR);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/admin/book";
        }
        return "redirect:/admin/login";
    }
}
