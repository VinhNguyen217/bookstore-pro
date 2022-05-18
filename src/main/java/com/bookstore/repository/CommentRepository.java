package com.bookstore.repository;

import com.bookstore.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT * FROM comment WHERE book_id = :bookId ORDER BY id DESC", nativeQuery = true)
    public List<Comment> getByBookId(Integer bookId);

    @Query(value = "SELECT * FROM comment WHERE book_id =:bookId and user_id =:userId", nativeQuery = true)
    public Comment getByBookIdAndUserId(Integer bookId, Integer userId);



}
