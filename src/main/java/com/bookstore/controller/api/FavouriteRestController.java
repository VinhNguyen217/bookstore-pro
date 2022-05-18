package com.bookstore.controller.api;

import com.bookstore.model.Favourite;
import com.bookstore.response.ResponseObject;
import com.bookstore.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favourite")
public class FavouriteRestController {

    @Autowired
    private FavouriteService favouriteService;

    @GetMapping("/qty")
    public ResponseEntity<?> getQtyFavouriteBook(@RequestParam("id") Integer bookId) {
        return ResponseObject.success(favouriteService.getQtyFavouriteByBookId(bookId));
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkFavouriteBookAndUser(@RequestParam("id") Integer bookId) {
        return ResponseObject.success(favouriteService.checkFavouriteBookAndUser(bookId));
    }

    @PostMapping("/like-book")
    public ResponseEntity<?> likeBook(@RequestBody Favourite favourite) {
        return ResponseObject.success(favouriteService.create(favourite));
    }

    @DeleteMapping("/unlike-book")
    public ResponseEntity<?> unlikeBook(@RequestParam("id") Integer bookId) {
        favouriteService.unlikeBook(bookId);
        return ResponseObject.success();
    }
}
