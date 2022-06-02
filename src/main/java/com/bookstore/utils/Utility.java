package com.bookstore.utils;

import com.bookstore.model.Bill;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


@Service
public class Utility {

    //Quy định mỗi trang có 12 sản phẩm
    private static Integer PAGE_SIZE = 8;

    private static Integer PAGE_SIZE_AUTHOR = 24;

    public static String getBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url = new StringBuffer();
        url.append(scheme).append("://").append(serverName);
        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if (url.toString().endsWith("/")) {
            url.append("/");
        }
        return url.toString();
    }

    /**
     * Tính số trang
     *
     * @param numberOfBooks
     * @return
     */
    public Integer getNumberOfPages(Integer numberOfBooks) {
        if (numberOfBooks <= PAGE_SIZE)
            return 1;
        else
            return numberOfBooks % PAGE_SIZE == 0 ? (numberOfBooks / PAGE_SIZE) : (numberOfBooks / PAGE_SIZE) + 1;
    }

    public Integer getNumberAuthorOfPages(Integer numberOfAuthors) {
        if (numberOfAuthors <= PAGE_SIZE_AUTHOR)
            return 1;
        else
            return numberOfAuthors % PAGE_SIZE_AUTHOR == 0 ? (numberOfAuthors / PAGE_SIZE_AUTHOR) : (numberOfAuthors / PAGE_SIZE_AUTHOR) + 1;
    }

    /**
     * Định dạng số tiền
     *
     * @param price
     * @return
     */
    public String formatPrice(Integer price) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeVN);
        return numberFormat.format(price);
    }

    /**
     * Định dạng thời gian
     *
     * @param date
     * @return
     */
    public String formatTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return formatter.format(date);
    }

    public String encode(String input) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(input);
    }

    public String formatStatus(String status) {
        if (status.equals(Bill.Status.WAIT_CONFIRM.toString()))
            return "Chưa xác nhận";
        else if (status.equals(Bill.Status.DELIVERY.toString()))
            return "Đang giao hàng";
        else if (status.equals(Bill.Status.DELIVERED.toString()))
            return "Giao hàng thành công";
        else if (status.equals(Bill.Status.CANCELLED.toString()))
            return "Đã hủy";
        else return "";
    }

    public String formatPayment(String payment) {
        if (payment.equals(Bill.Payment.CASH.toString()))
            return "Thanh toán tiền mặt khi nhận hàng";
        else if (payment.equals(Bill.Payment.PAYPAL.toString()))
            return "Thanh toán trực tuyến qua PayPal";
        else return "";
    }

    public double calculateToUSD(Integer price) {
        double priceConvert = Double.valueOf(price) / Double.valueOf(23198);
        return Math.round(priceConvert * 100.0) / 100.0;
    }
}
