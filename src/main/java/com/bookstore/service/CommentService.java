package com.bookstore.service;

import com.bookstore.model.Comment;
import com.bookstore.model.User;
import com.bookstore.repository.CommentRepository;
import com.bookstore.response.ResponseMessage;
import com.bookstore.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment create(Comment comment) {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            comment.setUserId(user.getId());
            comment.setCreatedAt(new Date());
            return commentRepository.save(comment);
        } catch (Exception ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }

    public List<Comment> getByBookId(Integer bookId) {
        return commentRepository.getByBookId(bookId);
    }

    /**
     * Trả về số nhận xét của quyển sách
     *
     * @param bookId
     * @return
     */
    public Integer getQtyCommentByBookId(Integer bookId) {
        return commentRepository.getByBookId(bookId).size();
    }

    public boolean checkCommentBookAndUser(Integer bookId) {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            User user = customUserDetails.getUser();
            Comment commentCheck = commentRepository.getByBookIdAndUserId(bookId, user.getId());
            return commentCheck != null ? true : false;
        } catch (Exception ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }

    public void deleteComment(Integer id) {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            Optional<Comment> commentOptional = commentRepository.findById(id);
            if (commentOptional.isPresent())
                commentRepository.deleteById(id);
            else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
        } catch (ClassCastException ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }
}
