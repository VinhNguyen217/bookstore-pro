package com.bookstore.controller.admin;

import com.bookstore.model.Promotion;
import com.bookstore.response.ResponseMessage;
import com.bookstore.service.BookService;
import com.bookstore.service.PromotionService;
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
import java.text.ParseException;

@Controller
@RequestMapping("/admin/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping({"/", ""})
    public String showPromotions(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            model.addAttribute("promotionList", promotionService.getAll());
            return "/admin/promotion/promotions";
        }
        return "redirect:/admin/login";
    }

    @GetMapping({"/{id}"})
    public String showPromotionById(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            Promotion promotion = promotionService.getById(id);
            model.addAttribute("promotion", promotion);
            model.addAttribute("title", "Chỉnh Sửa Thông Tin Khuyến Mãi");
            return "admin/promotion/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/add")
    public String showFormAdd(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            model.addAttribute("promotion", new Promotion());
            model.addAttribute("title", "Thêm Mới");
            return "admin/promotion/addOrEdit";
        }
        return "redirect:/admin/login";
    }

    @PostMapping("/save")
    public String saveOrEdit(Promotion promotion, RedirectAttributes ra) throws ParseException {
        promotionService.saveOrUpdate(promotion);
        ra.addFlashAttribute("msg_success", ResponseMessage.UPDATE_SUCCESS);
        return "redirect:/admin/promotion";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            try {
                promotionService.delete(id);
                ra.addFlashAttribute("msg_success", ResponseMessage.DELETE_SUCCESS);
            } catch (DataIntegrityViolationException ex) {
                ra.addFlashAttribute("msg_error", ResponseMessage.SQL_ERROR);
            }
            return "redirect:/admin/promotion";
        }
        return "redirect:/admin/login";
    }
}
