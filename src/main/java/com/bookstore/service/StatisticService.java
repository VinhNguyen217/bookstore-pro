package com.bookstore.service;

import com.bookstore.model.Bill;
import com.bookstore.model.BillDetail;
import com.bookstore.repository.BillDetailRepository;
import com.bookstore.repository.BillRepository;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private Utility utility;

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

    public Map<Integer, Integer> statisticRevenueByMonth(String time) {
        String[] timeArr = time.split("-");
        String month = timeArr[1];
        String year = timeArr[0];
        int numDays = utility.getNumDaysByMonth(month, year);
        List<Bill> billList = billRepository.getDeliveredBillByMonth(month, year);
        Map<Integer, Integer> res = new HashMap<>();
        Map<Integer, List<Bill>> groupByDay = billList.stream().collect(Collectors.groupingBy(s -> utility.getDayFromDate(s.getCreatedAt())));
        for (Map.Entry<Integer, List<Bill>> entry : groupByDay.entrySet()) {
            Integer totalByDay = 0;
            for (Bill bill : entry.getValue()) {
                totalByDay += bill.getTotal();
            }
            res.put(entry.getKey(), totalByDay);
        }
        for (int i = 1; i <= numDays; i++) {
            if (res.get(i) == null) res.put(i, 0);
        }
        return res;
    }
}
