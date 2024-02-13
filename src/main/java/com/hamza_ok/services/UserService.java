package com.hamza_ok.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.DAO.UserDao;
import com.hamza_ok.models.User;

public class UserService {
    private UserDao userDao;
    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User save(User user) {

        return userDao.createUser(user);
    }

    public User find(String id) {

        return userDao.getUserById(id);
    }

    public List<User> findAll() {

        return userDao.getAllUsers();
    }

    public User update(String id, User newUser) {

        return userDao.updateUser(id, newUser);
    }

    public void delete(String id) {

        userDao.deleteUser(id);
    }

}