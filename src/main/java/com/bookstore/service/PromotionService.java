package com.bookstore.service;

import com.bookstore.model.Promotion;
import com.bookstore.repository.PromotionRepository;
import com.bookstore.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    public List<Promotion> getAll() {
        return promotionRepository.getAllDescId();
    }

    public Promotion getById(Integer id) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(id);
        if (promotionOptional.isPresent())
            return promotionOptional.get();
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public Promotion saveOrUpdate(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public void delete(Integer id) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(id);
        if (promotionOptional.isPresent())
            promotionRepository.deleteById(id);
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

}
