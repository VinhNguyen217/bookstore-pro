package com.bookstore.controller.admin;

import com.bookstore.model.Author;
import com.bookstore.response.ResponseMessage;
import com.bookstore.security.SessionAdmin;
import com.bookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping({"/", ""})
    public String showAuthors(Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            model.addAttribute("authorList", authorService.getAllDescId());
            return "admin/author/authors";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/add")
    public String showFormAdd(Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            model.addAttribute("author", new Author());
            model.addAttribute("title", "Thêm Mới");
            return "admin/author/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/{id}")
    public String showAuthorById(@PathVariable("id") Integer id, Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            Author author = authorService.getById(id);
            model.addAttribute("author", author);
            model.addAttribute("title", "Tác giả : " + author.getName());
            return "admin/author/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
    public String saveOrUpdate(@ModelAttribute(name = "author") Author author,
                               @RequestParam("fileImage") MultipartFile fileImage,
                               RedirectAttributes ra) throws IOException {
        if (author.getId() == null) {
            if (fileImage.isEmpty())
                authorService.createNoImage(author);
            else
                authorService.createWithImage(author, fileImage);
        } else {
            authorService.update(author, fileImage);
        }
        ra.addFlashAttribute("msg_success", ResponseMessage.UPDATE_SUCCESS);
        return "redirect:/admin/author";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            try {
                authorService.delete(id);
                ra.addFlashAttribute("msg_success", ResponseMessage.DELETE_SUCCESS);
            } catch (DataIntegrityViolationException ex) {
                ra.addFlashAttribute("msg_error", ResponseMessage.SQL_ERROR);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/admin/author";
        }
        return "redirect:/admin/login";
    }
}
