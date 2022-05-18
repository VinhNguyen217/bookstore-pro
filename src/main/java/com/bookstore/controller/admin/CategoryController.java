package com.bookstore.controller.admin;

import com.bookstore.model.Category;
import com.bookstore.response.ResponseMessage;
import com.bookstore.security.SessionAdmin;
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

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/", ""})
    public String showCategories(Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            model.addAttribute("categoryList", categoryService.getAllDescId());
            return "admin/category/categories";
        }
        return "redirect:/admin/login";
    }

    @GetMapping({"/{id}"})
    public String showCategoryById(@PathVariable Integer id, Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            Category category = categoryService.getById(id);
            model.addAttribute("category", category);
            model.addAttribute("title", "Thể loại : " + category.getName());
            return "admin/category/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/add")
    public String showFormAdd(Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
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
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
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
