package com.bookstore.controller.admin;

import com.bookstore.model.Book;
import com.bookstore.response.ResponseMessage;
import com.bookstore.security.SessionAdmin;
import com.bookstore.service.*;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

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
    public String showBooks(Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            model.addAttribute("bookList", bookService.getAllDescId());
            model.addAttribute("categoryService", categoryService);
            model.addAttribute("authorService", authorService);
            model.addAttribute("utility", utility);
            return "admin/book/books";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/add")
    public String showFormAdd(Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            model.addAttribute("book", new Book());
            model.addAttribute("listCategories", categoryService.getAll());
            model.addAttribute("listAuthors", authorService.getAll());
            model.addAttribute("title", "Thêm Mới");
            return "admin/book/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/{id}")
    public String showBookById(@PathVariable("id") Integer id, Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
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
            if (fileImage.isEmpty()) {
                ra.addFlashAttribute("msg_error", ResponseMessage.IMAGE_NOT_NULL);
                return "redirect:/admin/book/add";
            } else {
                ra.addFlashAttribute("msg_success", ResponseMessage.UPDATE_SUCCESS);
                bookService.createBook(book, fileImage);
                return "redirect:/admin/book";
            }
        } else {
            ra.addFlashAttribute("msg_success", ResponseMessage.UPDATE_SUCCESS);
            bookService.updateBook(book, fileImage);
            return "redirect:/admin/book";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            try {
                bookService.delete(id);
                ra.addFlashAttribute("msg_success", ResponseMessage.DELETE_SUCCESS);
            } catch (DataIntegrityViolationException ex) {
                ra.addFlashAttribute("msg_error", ResponseMessage.SQL_ERROR);
            }
            return "redirect:/admin/book";
        }
        return "redirect:/admin/login";
    }
}
