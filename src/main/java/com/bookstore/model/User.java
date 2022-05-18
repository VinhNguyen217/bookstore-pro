package com.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.*;
import java.util.Date;

@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(200)")
    private String firstName;

    @Column(columnDefinition = "VARCHAR(200)")
    private String lastName;

    @Column(columnDefinition = "VARCHAR(200)", unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String verifyCode;

    private boolean isEnabled;

    @Column(name = "role", columnDefinition = "VARCHAR(200)")
    @Enumerated(EnumType.STRING)
    private Role role;

    private Date createdAt; //Thời gian tạo

    private Date verifiedAt;    //Thời gian cập nhật

    public enum Role {
        ROLE_ADMIN, ROLE_USER
    }

    public String getUserName() {
        return firstName + " " + lastName;
    }
}
