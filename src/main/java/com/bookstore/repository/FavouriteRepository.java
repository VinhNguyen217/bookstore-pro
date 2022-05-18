package com.bookstore.repository;

import com.bookstore.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {

    @Query(value = "SELECT * FROM favourite WHERE user_id = :userId", nativeQuery = true)
    List<Favourite> getByUserId(Integer userId);

    @Query(value = "SELECT * FROM favourite WHERE book_id = :bookId and user_id = :userId", nativeQuery = true)
    Favourite getByBookAndUser(Integer bookId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favourite WHERE book_id = :bookId and user_id = :userId ", nativeQuery = true)
    void deleteFavourite(Integer bookId, Integer userId);
}
