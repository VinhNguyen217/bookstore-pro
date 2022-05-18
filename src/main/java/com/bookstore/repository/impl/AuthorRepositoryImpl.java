package com.bookstore.repository.impl;

import com.bookstore.model.Author;
import com.bookstore.repository.custom.AuthorCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class AuthorRepositoryImpl implements AuthorCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Author> findAllAndPaging(Pageable pageable) {
        String sql = "SELECT * FROM author ORDER BY id DESC";
        String countSql = "SELECT count(*) FROM author ORDER BY id DESC";

        Query query = entityManager.createNativeQuery(sql, Author.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        Query countQuery = entityManager.createNativeQuery(countSql, Integer.class);

        List<Author> authorList = query.getResultList();
        int count = countQuery.getFirstResult();
        return new PageImpl<>(authorList, pageable, count).toList();
    }
}
