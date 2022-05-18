package com.bookstore.model.request;

import lombok.Data;

@Data
public class CartItemQty {
    private Integer itemId;
    private Integer quantity;
}
