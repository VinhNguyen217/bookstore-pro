package com.bookstore.repository.impl;

import com.bookstore.model.Book;
import com.bookstore.repository.custom.BookCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class BookRepositoryImpl implements BookCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Book> findByCategoryAndPaging(Integer catId, Pageable pageable) {
        String sql = " Select * from book WHERE category_id = :catId ORDER BY id DESC";
        String countSql = "Select count(*) from book WHERE category_id = :catId";

        Query query = entityManager.createNativeQuery(sql, Book.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        Query countQuery = entityManager.createNativeQuery(countSql, Integer.class);
        query.setParameter("catId", catId);
        countQuery.setParameter("catId", catId);

        List<Book> bookList = query.getResultList();
        int count = countQuery.getFirstResult();
        return new PageImpl<>(bookList, pageable, count).toList();
    }

    @Override
    public List<Book> findAllAndPaging(Pageable pageable) {
        String sql = "SELECT * FROM book ORDER BY id DESC";
        String countSql = "SELECT count(*) FROM book ORDER BY id DESC";

        Query query = entityManager.createNativeQuery(sql, Book.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        Query countQuery = entityManager.createNativeQuery(countSql, Integer.class);

        List<Book> bookList = query.getResultList();
        int count = countQuery.getFirstResult();
        return new PageImpl<>(bookList, pageable, count).toList();
    }

}
