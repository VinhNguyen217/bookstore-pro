package com.bookstore.controller.api;

import com.bookstore.model.User;
import com.bookstore.model.request.LoginRequest;
import com.bookstore.model.request.PasswordUpdate;
import com.bookstore.response.ResponseObject;
import com.bookstore.service.AuthService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody User user) throws MessagingException, UnsupportedEncodingException {
        return ResponseObject.success(userService.create(user));
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseObject.loginSuccess(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    @PutMapping("/user/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdate passwordUpdate) throws MessagingException, UnsupportedEncodingException {
        userService.updatePassword(passwordUpdate);
        return ResponseObject.success();
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {
        return ResponseObject.loginSuccess(authService.loginAdmin(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    @PostMapping("/admin/logout")
    public ResponseEntity<?> logoutAdmin() {
        authService.logoutAdmin();
        return ResponseObject.logoutSuccess();
    }

    @GetMapping("/admin/checkAdminSession")
    public ResponseEntity<?> checkAdminSession() {
        return ResponseObject.success(authService.checkAdminSession());
    }

}
