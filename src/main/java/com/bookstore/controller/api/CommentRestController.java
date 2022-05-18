package com.bookstore.controller.api;

import com.bookstore.model.Comment;
import com.bookstore.response.ResponseObject;
import com.bookstore.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/check")
    public ResponseEntity<?> checkCommentBookAndUser(@RequestParam("id") Integer bookId) {
        return ResponseObject.success(commentService.checkCommentBookAndUser(bookId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        return ResponseObject.success(commentService.create(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseObject.success();
    }
}
