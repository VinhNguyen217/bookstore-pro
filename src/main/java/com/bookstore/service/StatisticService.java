package com.bookstore.service;

import com.bookstore.model.Bill;
import com.bookstore.model.BillDetail;
import com.bookstore.repository.BillDetailRepository;
import com.bookstore.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticService {

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private BillRepository billRepository;

    /**
     * Tính số tiền bán ra từ một quyển sách khi biết id
     *
     * @param bookId
     * @return
     */
    public Integer getRevenueFromBookId(Integer bookId) {
        Integer total = 0;
        List<BillDetail> billDetails = billDetailRepository.getByBookId(bookId);
        if (!billDetails.isEmpty()) {
            for (BillDetail item : billDetails) {
                Optional<Bill> billOptional = billRepository.findById(item.getBillId());
                if (billOptional.isPresent()) {
                    Bill bill = billOptional.get();
                    if (Bill.Status.DELIVERED.equals(bill.getStatus()))
                        total += item.getQuantity() * item.getUnitPrice();
                }
            }
        }
        return total;
    }

    /**
     * Tính tổng tiền bán ra
     *
     * @return
     */
    public Integer getTotalRevenue() {
        Integer total = 0;
        List<Bill> billDeliveredList = billRepository.getDeliveredBill();
        for (Bill bill : billDeliveredList)
            total += bill.getTotal();
        return total;
    }
}
