package com.bookstore.service;

import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User login(String email, String password) {
        User userCheck = userRepository.findByEmail(email);
        if (userCheck == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.EMAIL_NOT_EXIST);
        } else {
            Boolean checkPassword = BCrypt.checkpw(password, userCheck.getPassword());
            if (checkPassword == false)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.PASSWORD_ERROR);
            else {
                if (userCheck.isEnabled()) {
                    if (userCheck.getRole() == User.Role.ROLE_USER) {
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
                        Authentication authentication = authenticationManager.authenticate(token);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return userCheck;
                    } else {
                        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.ACCESS_DENIED);
                    }
                } else {
                    throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.ACCOUNT_NOT_ACTIVE);
                }
            }
        }
    }

    public User loginAdmin(String email, String password, HttpSession session) {
        User userCheck = userRepository.findByEmail(email);
        if (userCheck == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.EMAIL_NOT_EXIST);
        } else {
            Boolean checkPassword = BCrypt.checkpw(password, userCheck.getPassword());
            if (checkPassword == false)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.PASSWORD_ERROR);
            else {
                if (userCheck.isEnabled()) {
                    if (userCheck.getRole() == User.Role.ROLE_ADMIN) {
                        session.setAttribute("admin", userCheck.getUserName());
                        return userCheck;
                    } else {
                        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.ACCESS_DENIED);
                    }
                } else {
                    throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.ACCOUNT_NOT_ACTIVE);
                }
            }
        }
    }

    public void logoutAdmin(HttpSession session) {
        session.removeAttribute("admin");
    }

    public String getUserName(HttpSession session) {
        return (String) session.getAttribute("admin");
    }
}
