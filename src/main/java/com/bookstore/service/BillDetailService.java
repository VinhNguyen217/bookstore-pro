package com.bookstore.service;

import com.bookstore.model.BillDetail;
import com.bookstore.repository.BillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillDetailService {

    @Autowired
    private BillDetailRepository billDetailRepository;

    public void create(BillDetail billDetail) {
        billDetailRepository.save(billDetail);
    }

    public List<BillDetail> getAllByBillId(Integer billId) {
        return billDetailRepository.getByBillId(billId);
    }
}
