package com.bookstore.repository.custom;

import com.bookstore.model.Author;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorCustomRepository {
    List<Author> findAllAndPaging(Pageable pageable);
}
