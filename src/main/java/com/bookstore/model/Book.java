package com.bookstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(200)")
    private String name;

    @Column(columnDefinition = "VARCHAR(200)")
    private String photo;

    @Column(columnDefinition = "INTEGER")
    private Integer quantity;

    @Column(columnDefinition = "INTEGER default 0")
    private Integer sold = 0;   //Số lượng sách đã bán

    @Column(columnDefinition = "INTEGER")
    private Integer priceImport;    //Giá nhập

    @Column(columnDefinition = "INTEGER")
    private Integer price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datePublish;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String publisher;

    @Column(columnDefinition = "INTEGER")
    private Integer author_id;

    @Column(columnDefinition = "INTEGER")
    private Integer category_id;

    @Column(columnDefinition = "INTEGER")
    private Integer numberOfLike = 0;   //Số lượt thích

    @Column(columnDefinition = "INTEGER")
    private Integer promotionId;
}
