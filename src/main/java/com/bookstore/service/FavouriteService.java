package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.Favourite;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.FavouriteRepository;
import com.bookstore.response.ResponseMessage;
import com.bookstore.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class FavouriteService {

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private BookRepository bookRepository;

    public Favourite create(Favourite favourite) {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            favourite.setUserId(user.getId());
            Optional<Book> bookOptional = bookRepository.findById(favourite.getBookId());
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setNumberOfLike(book.getNumberOfLike() + 1);
                bookRepository.save(book);
            }
            return favouriteRepository.save(favourite);
        } catch (ClassCastException ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }

    /**
     * Tìm số lượt thích của quyển sách khi biết id
     *
     * @param bookId
     * @return
     */
    public Integer getQtyFavouriteByBookId(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            return bookOptional.get().getNumberOfLike();
        } else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public boolean checkFavouriteBookAndUser(Integer bookId) {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            Favourite favouriteCheck = favouriteRepository.getByBookAndUser(bookId, user.getId());
            return favouriteCheck != null ? true : false;
        } catch (ClassCastException ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }

    public void unlikeBook(Integer bookId) {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            favouriteRepository.deleteFavourite(bookId, user.getId());
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setNumberOfLike(book.getNumberOfLike() - 1);
                bookRepository.save(book);
            }
        } catch (ClassCastException ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }

    public List<Favourite> getByUserId() {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            return favouriteRepository.getByUserId(user.getId());
        } catch (ClassCastException ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }
}
