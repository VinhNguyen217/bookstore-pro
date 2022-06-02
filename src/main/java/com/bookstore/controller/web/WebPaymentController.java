package com.bookstore.controller.web;

import com.bookstore.model.Bill;
import com.bookstore.model.request.PaypalModel;
import com.bookstore.service.*;
import com.bookstore.utils.CommonStringUtil;
import com.bookstore.utils.Utility;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@Controller
public class WebPaymentController {

    private static final Logger log = LoggerFactory.getLogger(WebPaymentController.class);

    @Autowired
    private BillService billService;

    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    @Autowired
    private Utility utility;

    @Autowired
    private PaypalService paypalService;

    @GetMapping("/payment")
    public String showPayment(Model model) {
        Bill billPrevious = billService.getLastBill();
        if (billPrevious != null)
            model.addAttribute("bill", billPrevious);
        else
            model.addAttribute("bill", new Bill());
        model.addAttribute("itemList", cartService.getListSelectedItem());
        model.addAttribute("bookService", bookService);
        model.addAttribute("utility", utility);
        model.addAttribute("total", cartService.getTotalPrices());
        return "web/payment";
    }

    @PostMapping("/payment/create_bill")
    public String payment(Bill bill, HttpServletRequest request, RedirectAttributes ra) throws MessagingException, IOException {
        if (Bill.Payment.CASH.equals(bill.getPayment())) {
            billService.create(bill);
            ra.addFlashAttribute("createBill", true);
        } else {
            request.getSession().setAttribute(CommonStringUtil.MY_SESSION_BILL, bill);
            String cancelUrl = Utility.getBaseURL(request) + "/" + CommonStringUtil.URL_PAYPAL_CANCEL;
            String successUrl = Utility.getBaseURL(request) + "/" + CommonStringUtil.URL_PAYPAL_SUCCESS;
            PaypalModel paypalModel = new PaypalModel();
            Integer totalPayment = cartService.getTotalPrices();
            paypalModel.setTotal(utility.calculateToUSD(totalPayment));
            paypalModel.setCurrency("USD");
            paypalModel.setCancelUrl(cancelUrl);
            paypalModel.setSuccessUrl(successUrl);
            try {
                Payment payment = paypalService.createPayment(paypalModel);
                for (Links links : payment.getLinks()) {
                    if (links.getRel().equals("approval_url")) {
                        return "redirect:" + links.getHref();
                    }
                }
            } catch (PayPalRESTException e) {
                log.error(e.getMessage());
            }
        }
        return "redirect:/payment";
    }

}
