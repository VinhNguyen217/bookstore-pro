package com.bookstore.controller.api;

import com.bookstore.response.ResponseObject;
import com.bookstore.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotion")
public class PromotionRestController {

    @Autowired
    private PromotionService promotionService;

    @PutMapping("/updateDisplay/{id}")
    public ResponseEntity<?> updateDisplay(@PathVariable Integer id) {
        promotionService.updateDisplay(id);
        return ResponseObject.success();
    }
}
