package com.bookstore.controller.api;

import com.bookstore.model.CartItem;
import com.bookstore.model.request.CartItemQty;
import com.bookstore.model.request.CartItemSelect;
import com.bookstore.response.ResponseObject;
import com.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItem cartItem) {
        return ResponseObject.success(cartService.create(cartItem));
    }

    /**
     * Lấy số lượng sản phẩm trong giỏ hàng
     *
     * @return
     */
    @GetMapping("/qty")
    public ResponseEntity<?> getNumberOfCartItem() {
        return ResponseObject.success(cartService.getNumberOfCartItem());
    }

    /**
     * Cập nhật số lượng sản phẩm cho một item
     *
     * @param cartItemQty
     */
    @PutMapping("/item/update")
    public ResponseEntity<?> updateCartItem(@RequestBody CartItemQty cartItemQty) {
        return ResponseObject.success(cartService.updateCartItem(cartItemQty));
    }

    /**
     * Chọn item để thanh toán
     */
    @PutMapping("/item/selectOrUnSelect")
    public ResponseEntity<?> selectCartItem(@RequestBody CartItemSelect cartItemSelect) {
        return ResponseObject.success(cartService.selectCartItem(cartItemSelect));
    }

    /**
     * Lấy tổng tiền dựa trên những item được lựa chọn
     *
     * @return
     */
    @GetMapping("/total")
    public ResponseEntity<?> getTotalPrices() {
        return ResponseObject.success(cartService.getTotalPrices());
    }

    /**
     * Xóa một item trong giỏ hàng khi biết id
     *
     * @param id
     */
    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("id") Integer id) {
        cartService.deleteCartItem(id);
        return ResponseObject.success();
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        cartService.clearCart();
        return ResponseObject.success();
    }
}
