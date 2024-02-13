package com.hamza_ok.DAO;

import java.util.List;

import com.hamza_ok.models.User;

// CRUD Operations
public interface UserDao {

    User createUser(User user);

    User getUserById(String id);

    List<User> getAllUsers();

    User updateUser(String id, User newUser);

    void deleteUser(String id);

}