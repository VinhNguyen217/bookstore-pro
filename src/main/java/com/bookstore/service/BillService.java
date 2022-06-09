package com.bookstore.service;

import com.bookstore.model.Bill;
import com.bookstore.model.BillDetail;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.request.OrderUpdate;
import com.bookstore.repository.BillRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.response.ResponseMessage;
import com.bookstore.security.CustomUserDetails;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private BillDetailService billDetailService;

    @Autowired
    private BookService bookService;

    @Autowired
    private Utility utility;

    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    TemplateEngine templateEngine;

    public List<Bill> getAllDescId() {
        return billRepository.getAllDescId();
    }

    public Bill getById(Integer id) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isPresent())
            return billOptional.get();
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    /**
     * Lấy ra hóa đơn cuối cùng của tài khoản đang đăng nhập
     *
     * @return
     */
    public Bill getLastBill() {
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        Integer userId = customUserDetails.getUser().getId();
        return billRepository.getLastBill(userId);
    }

    /**
     * Tạo hóa đơn
     * @param bill
     */
    public void create(Bill bill) throws MessagingException, UnsupportedEncodingException {
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        Integer userId = customUserDetails.getUser().getId();
        if (bill != null) {
            Integer totalPayment = cartService.getTotalPrices();
            bill.setTotal(totalPayment);
            bill.setUserId(userId);
            bill.setStatus(Bill.Status.WAIT_CONFIRM);
            bill.setCreatedAt(new Date());
            Bill billCreated = billRepository.save(bill);
            List<CartItem> itemSelectedList = cartService.getListSelectedItem();
            itemSelectedList.stream().forEach((item) -> {
                BillDetail billDetail = new BillDetail();
                Book book = bookService.getById(item.getBookId());
                billDetail.setBillId(billCreated.getId());
                billDetail.setBookId(item.getBookId());
                billDetail.setQuantity(item.getQuantity());
                if (book.getPromotionId() == null) {
                    billDetail.setUnitPrice(bookService.getById(item.getBookId()).getPrice());
                } else if (book.getPromotionId() == 24) {
                    billDetail.setUnitPrice(bookService.getById(item.getBookId()).getPrice());
                } else {
                    billDetail.setUnitPrice(bookService.calculatePromotionalMoney(book));
                }
                billDetailService.create(billDetail);
            });
            sendEmailBill(bill, itemSelectedList);
            cartService.deleteCartItemSelected();
        }
    }

    /**
     * Gửi hóa đơn qua email
     *
     * @param bill
     * @param itemSelectedList
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendEmailBill(Bill bill, List<CartItem> itemSelectedList) throws MessagingException, UnsupportedEncodingException {
        String subject = "Xác nhận đơn hàng";
        String senderName = "BookStore";

        Locale locale = LocaleContextHolder.getLocale();
        Context ctx = new Context(locale);
        ctx.setVariable("bookService", bookService);
        ctx.setVariable("utility", utility);
        ctx.setVariable("itemList", itemSelectedList);
        ctx.setVariable("bill", bill);

        String htmlContent = "";
        htmlContent = templateEngine.process("web/email_bill.html", ctx);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom("bookstorevn217@gmail.com", senderName);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setTo(bill.getEmail());
        mimeMessageHelper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     *
     * @param orderUpdate
     * @return
     */
    public Bill updateStatusBill(OrderUpdate orderUpdate) {
        Optional<Bill> optionalBill = billRepository.findById(orderUpdate.getOrderId());
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            Bill.Status status = orderUpdate.getStatus();

            switch (status) {
                case DELIVERY:
                    if (Bill.Status.CANCELLED.equals(bill.getStatus())) {
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.UPDATE_ORDER_DELIVERY_ERROR);
                    }
                    bill.setStatus(status);
                    bill.setConfirmAt(new Date());
                    break;
                case DELIVERED:
                    if (Bill.Status.CANCELLED.equals(bill.getStatus())) {
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.UPDATE_ORDER_DELIVERED_ERROR);
                    }
                    bill.setStatus(status);
                    bill.setUpdatedAt(new Date());
                    List<BillDetail> billDetails = billDetailService.getAllByBillId(orderUpdate.getOrderId());
                    billDetails.stream().forEach(billDetail -> {
                        Integer soldCurrent = bookService.getById(billDetail.getBookId()).getSold();    //Số lượng đã bán hiện tại
                        Integer soldUpdate = soldCurrent + billDetail.getQuantity();    //Số lượng đã bán sau khi cập nhật
                        bookRepository.updateSold(billDetail.getBookId(), soldUpdate);
                    });
                    break;
                case CANCELLED:
                    if (Bill.Status.DELIVERED.equals(bill.getStatus())) {
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.CANCELLED_ORDER_ERROR);
                    }
                    bill.setStatus(status);
                    bill.setUpdatedAt(new Date());
            }
            return billRepository.save(bill);
        } else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public List<Bill> getByUserId() {
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        Integer userId = customUserDetails.getUser().getId();
        return billRepository.getByUserId(userId);
    }

    public List<Bill> getByUserAndStatus(Bill.Status status) {
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        Integer userId = customUserDetails.getUser().getId();
        return billRepository.getByUserIdAndStatus(userId, status.toString());
    }

}
