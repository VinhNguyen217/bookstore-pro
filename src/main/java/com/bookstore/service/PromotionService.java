package com.bookstore.service;

import com.bookstore.model.Promotion;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.PromotionRepository;
import com.bookstore.response.ResponseMessage;
import com.bookstore.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.text.html.Option;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private Utility utility;

    public List<Promotion> getAll() {
        return promotionRepository.getAllDescId();
    }

    public Promotion getById(Integer id) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(id);
        if (promotionOptional.isPresent())
            return promotionOptional.get();
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public Promotion saveOrUpdate(Promotion promotion) throws ParseException {
        String endDate = utility.convertDateToStr(promotion.getEndDate());
        endDate += " 23:59:59";
        Date endDateConvert = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
        promotion.setEndDate(endDateConvert);
        return promotionRepository.save(promotion);
    }

    public void delete(Integer id) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(id);
        if (promotionOptional.isPresent())
            promotionRepository.deleteById(id);
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkPromotion() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(date);
        List<Promotion> promotionExpired = promotionRepository.getPromotionExpired(currentDate);
        if (!promotionExpired.isEmpty()) {
            promotionExpired.stream().forEach((promotion) -> {
                bookRepository.updateBookByPromotionExpired(promotion.getId());
            });
        }
    }
}
