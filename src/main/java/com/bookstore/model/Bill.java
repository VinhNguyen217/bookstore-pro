package com.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(200)")
    private String receiver;

    @Column(columnDefinition = "VARCHAR(200)")
    private String phone;

    @Column(columnDefinition = "VARCHAR(200)")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(columnDefinition = "INTEGER default '0'")
    private Integer total;

    @Column(columnDefinition = "INTEGER")
    private Integer userId;

    @Column(name = "status", columnDefinition = "VARCHAR(200)")
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private Date createdAt; //thời gian tạo hóa đơn

    private Date confirmAt; //thời gian xác nhận hóa đơn

    private Date updatedAt; //thời gian cập nhật hóa đơn có thể là hủy hoặc giao hàng thành công

    @Column(name = "payment", columnDefinition = "VARCHAR(200)")
    @Enumerated(EnumType.STRING)
    private Payment payment;

    public enum Status {
        WAIT_CONFIRM, DELIVERY, DELIVERED, CANCELLED
    }

    public enum Payment {
        CASH, PAYPAL
    }
}
