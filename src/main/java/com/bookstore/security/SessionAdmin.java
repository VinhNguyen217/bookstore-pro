package com.bookstore.security;

import com.bookstore.model.User;

import java.util.LinkedList;
import java.util.Queue;

public class SessionAdmin {
    public Queue<User> userList;

    private static SessionAdmin instance = null;

    private SessionAdmin() {
        userList = new LinkedList<>();
    }

    public static SessionAdmin getInstance() {
        if (instance == null) {
            instance = new SessionAdmin();
        }
        return instance;
    }

}
