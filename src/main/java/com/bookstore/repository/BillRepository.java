package com.bookstore.repository;

import com.bookstore.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query(value = "SELECT * FROM bill ORDER BY id DESC", nativeQuery = true)
    List<Bill> getAllDescId();

    @Query(value = "SELECT * FROM bill WHERE user_id = :id ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Bill getLastBill(Integer id);

    @Query(value = "SELECT * FROM bill WHERE id = :id and token = :token and status = 'WAIT_CONFIRM'and user_id = :uId", nativeQuery = true)
    Bill getBillByIdAndToken(Integer id, String token, Integer uId);

    @Query(value = "SELECT * FROM bill WHERE user_id = :id ORDER BY id DESC", nativeQuery = true)
    List<Bill> getByUserId(Integer id);

    @Query(value = "SELECT * FROM bill WHERE user_id = :id and status = :status ORDER BY id DESC", nativeQuery = true)
    List<Bill> getByUserIdAndStatus(Integer id, String status);

    @Query(value = "SELECT * FROM bill WHERE status = 'WAIT_CONFIRM'", nativeQuery = true)
    List<Bill> getWaitConfirmBill();

    @Query(value = "SELECT * FROM bill WHERE status = 'DELIVERED'", nativeQuery = true)
    List<Bill> getDeliveredBill();
}
