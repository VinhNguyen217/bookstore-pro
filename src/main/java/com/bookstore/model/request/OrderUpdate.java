package com.bookstore.model.request;

import com.bookstore.model.Bill;
import lombok.Data;

@Data
public class OrderUpdate {
    private Integer orderId;
    private Bill.Status status;
}
