package com.bookstore.controller.admin;

import com.bookstore.model.Bill;
import com.bookstore.model.BillDetail;
import com.bookstore.model.Book;
import com.bookstore.security.SessionAdmin;
import com.bookstore.service.*;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/statistic")
public class StatisticController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BillService billService;

    @Autowired
    private BillDetailService billDetailService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private Utility utility;

    @GetMapping("/revenue")
    public String showFormRevenueStatistics(Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            List<Book> bookList = bookService.getAll();
            model.addAttribute("bookList", bookList);
            model.addAttribute("utility", utility);
            model.addAttribute("statisticService", statisticService);
            Integer amountInput = 0, amountSold = 0, totalInput = 0;
            Integer totalSold = statisticService.getTotalRevenue();
            for (Book book : bookList) {
                amountInput += book.getQuantity();
                amountSold += book.getSold();
                totalInput += book.getQuantity() * book.getPriceImport();
            }
            model.addAttribute("amountInput", amountInput);
            model.addAttribute("amountSold", amountSold);
            model.addAttribute("totalInput", totalInput);
            model.addAttribute("totalSold", totalSold);
            return "admin/statistic/revenue";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/order")
    public String showFormOrder(Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            List<Bill> billList = billService.getAllDescId();
            model.addAttribute("billList", billList);
            model.addAttribute("utility", utility);
            long countWaitConfirm = billList.stream().filter(bill -> Bill.Status.WAIT_CONFIRM.equals(bill.getStatus())).count();
            long countDelivery = billList.stream().filter(bill -> Bill.Status.DELIVERY.equals(bill.getStatus())).count();
            long countDelivered = billList.stream().filter(bill -> Bill.Status.DELIVERED.equals(bill.getStatus())).count();
            long countCancelled = billList.stream().filter(bill -> Bill.Status.CANCELLED.equals(bill.getStatus())).count();
            model.addAttribute("countWaitConfirm", countWaitConfirm);
            model.addAttribute("countDelivery", countDelivery);
            model.addAttribute("countDelivered", countDelivered);
            model.addAttribute("countCancelled", countCancelled);
            return "admin/statistic/order";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/order/{id}")
    public String showOrderById(@PathVariable("id") Integer id, Model model) {
        if (!SessionAdmin.getInstance().userList.isEmpty()) {
            Bill bill = billService.getById(id);
            List<BillDetail> billDetailList = billDetailService.getAllByBillId(id);
            model.addAttribute("bill", bill);
            model.addAttribute("billDetailList", billDetailList);
            model.addAttribute("bookService", bookService);
            model.addAttribute("utility", utility);
            return "admin/statistic/orderDetail";
        }
        return "redirect:/admin/login";
    }
}
