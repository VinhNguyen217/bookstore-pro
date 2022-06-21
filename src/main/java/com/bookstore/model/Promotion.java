package com.bookstore.model;

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
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "INTEGER")
    private Integer reduceNumber;   //Số lượng giảm

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;   //ngày bắt đầu

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;   //ngày kết thúc
}
