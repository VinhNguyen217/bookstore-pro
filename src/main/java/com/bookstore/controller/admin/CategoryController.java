package com.bookstore.controller.admin;

import com.bookstore.model.Category;
import com.bookstore.response.ResponseMessage;
import com.bookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/", ""})
    public String showCategories(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            model.addAttribute("categoryList", categoryService.getAllDescId());
            return "admin/category/categories";
        }
        return "redirect:/admin/login";
    }

    @GetMapping({"/{id}"})
    public String showCategoryById(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            Category category = categoryService.getById(id);
            model.addAttribute("category", category);
            model.addAttribute("title", "Thể loại : " + category.getName());
            return "admin/category/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/add")
    public String showFormAdd(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            model.addAttribute("category", new Category());
            model.addAttribute("title", "Thêm Mới");
            return "admin/category/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @PostMapping("/save")
    public String saveOrEdit(Category category, RedirectAttributes ra) {
        categoryService.saveOrUpdate(category);
        ra.addFlashAttribute("msg_success", ResponseMessage.UPDATE_SUCCESS);
        return "redirect:/admin/category";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            try {
                categoryService.delete(id);
                ra.addFlashAttribute("msg_success", ResponseMessage.DELETE_SUCCESS);
            } catch (DataIntegrityViolationException ex) {
                ra.addFlashAttribute("msg_error", ResponseMessage.SQL_ERROR);
            }
            return "redirect:/admin/category";
        }
        return "redirect:/admin/login";
    }
}
