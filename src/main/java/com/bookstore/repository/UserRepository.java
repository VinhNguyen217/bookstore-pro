package com.bookstore.repository;

import com.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    User findByEmail(String email);

    @Query(value = "SELECT * FROM user WHERE email = :email and password = :password", nativeQuery = true)
    Optional<User> findByEmailAndPassword(String email, String password);

    @Query(value = "SELECT * FROM user WHERE verify_code = :code and is_enabled = false", nativeQuery = true)
    User findByVerifyCode(String code);
}
