package com.bookstore.repository;

import com.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT * FROM category WHERE name =:name LIMIT 1", nativeQuery = true)
    List<Category> findByName(String name);

    @Query(value = "SELECT * FROM category ORDER BY id DESC", nativeQuery = true)
    List<Category> getAllDescId();
}
