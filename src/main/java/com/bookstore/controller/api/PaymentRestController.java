package com.bookstore.controller.api;

import com.bookstore.model.request.OrderUpdate;
import com.bookstore.response.ResponseObject;
import com.bookstore.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentRestController {

    @Autowired
    private BillService billService;

    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateStatusBill(@RequestBody OrderUpdate orderUpdate) {
        return ResponseObject.success(billService.updateStatusBill(orderUpdate));
    }
}
