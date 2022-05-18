package com.bookstore.repository;

import com.bookstore.model.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {

    @Query(value = "SELECT * FROM bill_detail WHERE bill_id = :billId", nativeQuery = true)
    List<BillDetail> getByBillId(Integer billId);

    @Query(value = "SELECT * FROM bill_detail WHERE book_id = :bookId", nativeQuery = true)
    List<BillDetail> getByBookId(Integer bookId);
}
