package com.bookstore.controller.web;

import com.bookstore.model.Bill;
import com.bookstore.service.BillService;
import com.bookstore.service.PaypalService;
import com.bookstore.utils.CommonStringUtil;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;

import static com.bookstore.utils.CommonStringUtil.URL_PAYPAL_CANCEL;
import static com.bookstore.utils.CommonStringUtil.URL_PAYPAL_SUCCESS;

@Controller
public class WebPaypalController {

    private static final Logger log = LoggerFactory.getLogger(WebPaypalController.class);

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private BillService billService;

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "web/paypal/cancel";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpSession session) throws MessagingException, UnsupportedEncodingException {
        try {
            Bill billTemp = (Bill) session.getAttribute(CommonStringUtil.MY_SESSION_BILL);
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                billService.create(billTemp);
                session.setAttribute(CommonStringUtil.MY_SESSION_BILL,null);
                return "web/paypal/success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/payment";
    }
}
