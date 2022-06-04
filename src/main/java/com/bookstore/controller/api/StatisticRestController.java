package com.bookstore.controller.api;

import com.bookstore.response.ResponseObject;
import com.bookstore.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic")
public class StatisticRestController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/revenueByMonth")
    public ResponseEntity<?> statisticRevenueByMonth(@RequestParam String month) {
        return ResponseObject.success(statisticService.statisticRevenueByMonth(month));
    }

}
