package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.User;
import com.bookstore.model.request.CartItemQty;
import com.bookstore.model.request.CartItemSelect;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.response.ResponseMessage;
import com.bookstore.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Tạo 1 item trong giỏ hàng
     *
     * @param cartItem
     * @return
     */
    public CartItem create(CartItem cartItem) {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            CartItem cartItemCheck = cartItemRepository.findByUserAndProduct(user.getId(), cartItem.getBookId());
            if (cartItemCheck != null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.CART_ITEM_EXIST);
            else {
                cartItem.setUserId(user.getId());
                return cartItemRepository.save(cartItem);
            }
        } catch (ClassCastException ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }

    /**
     * Lấy danh sách item trong giỏ hàng của khách hàng đang đăng nhập
     *
     * @return
     */
    public List<CartItem> findByUser() {
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        Integer userId = customUserDetails.getUser().getId();
        return cartItemRepository.findByUser(userId);
    }

    /**
     * Tính số lượng sản phẩm trong giỏ hàng
     *
     * @return
     */
    public Integer getNumberOfCartItem() {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            List<CartItem> cartItemList = cartItemRepository.findByUser(user.getId());
            return cartItemList.size();
        } catch (ClassCastException ex) {
            return 0;
        }
    }

    /**
     * Cập nhật số lượng sản phẩm cho một item
     *
     * @param cartItemQty
     * @return
     */
    public CartItem updateCartItem(CartItemQty cartItemQty) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemQty.getItemId());
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItemQty.getQuantity());
            return cartItemRepository.save(cartItem);
        } else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.CART_ITEM_NOT_FOUND);
    }

    /**
     * Chọn item để thanh toán
     *
     * @param cartItemSelect
     */
    public CartItem selectCartItem(CartItemSelect cartItemSelect) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemSelect.getItemId());
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setSelected(cartItemSelect.isSelect());
            return cartItemRepository.save(cartItem);
        } else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.CART_ITEM_NOT_FOUND);
    }

    /**
     * Lấy tổng tiền dựa trên những item được lựa chọn
     *
     * @return
     */
    public Integer getTotalPrices() {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            Integer userId = customUserDetails.getUser().getId();
            List<CartItem> cartItemList = cartItemRepository.findCartItemsSelected(userId);
            if (cartItemList.size() > 0) {
                int totalPrices = 0;
                for (int i = 0; i < cartItemList.size(); i++) {
                    CartItem cartItem = cartItemList.get(i);
                    Book bookSelected = bookRepository.findById(cartItem.getBookId()).get();
                    totalPrices += cartItem.getQuantity() * bookSelected.getPrice();
                }
                return totalPrices;
            } else
                return 0;
        } catch (ClassCastException ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }


    /**
     * Lấy danh sách những item được chọn
     *
     * @return
     */
    public List<CartItem> getListSelectedItem() {
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        Integer userId = customUserDetails.getUser().getId();
        return cartItemRepository.findCartItemsSelected(userId);
    }

    /**
     * Xóa item trong giỏ hàng khi biết id
     *
     * @param id
     */
    public void deleteCartItem(Integer id) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        if (cartItemOptional.isPresent()) {
            cartItemRepository.deleteById(id);
        } else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.CART_ITEM_NOT_FOUND);
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void clearCart() {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            cartItemRepository.clearCart(user.getId());
        } catch (ClassCastException ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }

    /**
     * Xóa những item đã được chọn trong giỏ hàng của user đang đăng nhập
     */
    public void deleteCartItemSelected() {
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        Integer userId = customUserDetails.getUser().getId();
        cartItemRepository.deleteCartItemSelected(userId);
    }
}
