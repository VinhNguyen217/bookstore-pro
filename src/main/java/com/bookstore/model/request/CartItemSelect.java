package com.bookstore.model.request;

import lombok.Data;

@Data
public class CartItemSelect {
    private Integer itemId;
    private boolean select;
}
