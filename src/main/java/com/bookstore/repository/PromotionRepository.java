package com.bookstore.repository;

import com.bookstore.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    @Query(value = "SELECT * FROM promotion ORDER BY id DESC", nativeQuery = true)
    List<Promotion> getAllDescId();

}
