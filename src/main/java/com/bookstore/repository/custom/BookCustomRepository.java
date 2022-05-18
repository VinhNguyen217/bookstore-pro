package com.bookstore.repository.custom;

import com.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookCustomRepository {
    List<Book> findByCategoryAndPaging(Integer catId, Pageable pageable);

    List<Book> findAllAndPaging(Pageable pageable);

}
