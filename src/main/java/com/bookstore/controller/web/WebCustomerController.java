package com.bookstore.controller.web;

import com.bookstore.model.Bill;
import com.bookstore.model.BillDetail;
import com.bookstore.model.Favourite;
import com.bookstore.model.User;
import com.bookstore.service.*;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class WebCustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    @Autowired
    private BillDetailService billDetailService;

    @Autowired
    private Utility utility;

    @Autowired
    private BookService bookService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/account")
    public String showInfoAccount(Model model) {
        User user = userService.getUserSession();
        model.addAttribute("user", user);
        model.addAttribute("pageCurrent", "account");
        return "web/customer/account";
    }

    @PostMapping("/account/update")
    public String updateAccount(User user, RedirectAttributes ra) {
        userService.updateUser(user);
        ra.addFlashAttribute("msg_update_account", "Cập nhật thành công");
        return "redirect:/customer/account";
    }

    @GetMapping("/account/update-password")
    public String updatePassword(Model model) {
        model.addAttribute("pageCurrent", "account");
        return "web/customer/update_password";
    }

    @GetMapping("/order")
    public String showInfoOrder(Model model) {
        List<Bill> billAllList = billService.getByUserId();
        List<Bill> billWaitConfirmList = billService.getByUserAndStatus(Bill.Status.WAIT_CONFIRM);
        List<Bill> billDeliveryList = billService.getByUserAndStatus(Bill.Status.DELIVERY);
        List<Bill> billDeliveredList = billService.getByUserAndStatus(Bill.Status.DELIVERED);
        List<Bill> billCancelledList = billService.getByUserAndStatus(Bill.Status.CANCELLED);
        model.addAttribute("billAllList", billAllList);
        model.addAttribute("billWaitConfirmList", billWaitConfirmList);
        model.addAttribute("billDeliveryList", billDeliveryList);
        model.addAttribute("billDeliveredList", billDeliveredList);
        model.addAttribute("billCancelledList", billCancelledList);
        model.addAttribute("billDetailService", billDetailService);
        model.addAttribute("utility", utility);
        model.addAttribute("bookService", bookService);
        model.addAttribute("pageCurrent", "order");
        return "web/customer/order";
    }

    @GetMapping("/order/view/{id}")
    public String showDetailOrder(@PathVariable("id") Integer id, Model model) {
        Bill bill = billService.getById(id);
        List<BillDetail> billDetailList = billDetailService.getAllByBillId(id);
        model.addAttribute("bill", bill);
        model.addAttribute("billDetailList", billDetailList);
        model.addAttribute("bookService", bookService);
        model.addAttribute("utility", utility);
        model.addAttribute("pageCurrent", "order");
        return "web/customer/order_detail";
    }

    @GetMapping("/favourite")
    public String showFavoriteProduct(Model model) {
        List<Favourite> favouriteList = favouriteService.getByUserId();
        model.addAttribute("pageCurrent", "favourite");
        model.addAttribute("favouriteList", favouriteList);
        model.addAttribute("bookService", bookService);
        model.addAttribute("utility", utility);
        model.addAttribute("favouriteService", favouriteService);
        model.addAttribute("commentService", commentService);
        return "web/customer/favourite";
    }
}
