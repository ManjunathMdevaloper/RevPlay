package com.revplay.main;

import com.revplay.dao.UserDAO;
import com.revplay.dao.UserDAOImpl;
import com.revplay.model.User;

public class UserLoginTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAOImpl();

        User user = userDAO.login("rahul@gmail.com", "r123");

        if (user != null) {
            System.out.println("Login Successful!");
            System.out.println("Welcome " + user.getName());
            System.out.println("Role: " + user.getRole());
        } else {
            System.out.println("Invalid credentials");
        }
    }
}
