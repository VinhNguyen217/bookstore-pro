package com.bookstore.repository;

import com.bookstore.model.Author;
import com.bookstore.model.Category;
import com.bookstore.repository.custom.AuthorCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>, AuthorCustomRepository {

    @Query(value = "SELECT * FROM author ORDER BY name", nativeQuery = true)
    List<Author> getAllBySorted();

    @Query(value = "SELECT * FROM author ORDER BY id DESC", nativeQuery = true)
    List<Author> getAllDescId();
}
