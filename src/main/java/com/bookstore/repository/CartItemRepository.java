package com.bookstore.repository;

import com.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query(value = "SELECT * FROM cart_item WHERE user_id = :id", nativeQuery = true)
    List<CartItem> findByUser(Integer id);

    @Query(value = "SELECT * FROM cart_item WHERE user_id = :userId AND book_id = :bookId", nativeQuery = true)
    CartItem findByUserAndProduct(Integer userId, Integer bookId);

    @Query(value = "SELECT * FROM cart_item WHERE user_id = :id and selected = 1", nativeQuery = true)
    List<CartItem> findCartItemsSelected(Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE user_id = :id", nativeQuery = true)
    void clearCart(Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE user_id = :id AND selected = 1", nativeQuery = true)
    void deleteCartItemSelected(Integer id);
}
