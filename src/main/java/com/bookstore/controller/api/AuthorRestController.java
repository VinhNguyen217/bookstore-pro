package com.bookstore.controller.api;

import com.bookstore.response.ResponseObject;
import com.bookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/author")
public class AuthorRestController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam("name") String name) {
        return ResponseObject.success(authorService.findByName(name));
    }
}
