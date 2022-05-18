package com.bookstore.repository;

import com.bookstore.model.Book;
import com.bookstore.repository.custom.BookCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, BookCustomRepository, PagingAndSortingRepository<Book, Integer> {

    @Query(value = "SELECT * FROM book ORDER BY id DESC", nativeQuery = true)
    List<Book> getAllDescId();

    // Tìm 10 quyển sách mới nhất theo thứ tự giảm dần theo id
    @Query(value = "SELECT * FROM book ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<Book> findByNew();

    // Tìm 10 quyển sách bán chạy nhất theo số lượng bán giảm dần
    @Query(value = "SELECT * FROM book ORDER BY sold DESC LIMIT 10", nativeQuery = true)
    List<Book> findBySold();

    //Tìm 10 quyển sách có số lượt thích cao nhất giảm dần
    @Query(value = "SELECT * FROM book ORDER BY number_of_like DESC LIMIT 10", nativeQuery = true)
    List<Book> findByLike();

    //Tìm 10 quyển sách cùng danh mục và có id khác với id quyển sách hiện tại
    @Query(value = "SELECT * FROM book WHERE book.category_id = :catId and book.id != :bookId ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<Book> findSimilar(Integer catId, Integer bookId);

    @Query(value = "SELECT * FROM book WHERE book.category_id = :catId", nativeQuery = true)
    List<Book> findByCategory(Integer catId);

    @Query(value = "SELECT * FROM book WHERE book.author_id = :authorId", nativeQuery = true)
    List<Book> findByAuthor(Integer authorId);

    @Query(value = "SELECT * FROM book WHERE book.publisher_id = :publisherId", nativeQuery = true)
    List<Book> findByPublisher(Integer publisherId);

    //Tìm sách có tên chứa ký tự người nhập
    @Query(value = "SELECT * FROM book WHERE name like %:name%", nativeQuery = true)
    List<Book> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE book SET sold = :sold WHERE id = :id", nativeQuery = true)
    void updateSold(Integer id, Integer sold);
}
