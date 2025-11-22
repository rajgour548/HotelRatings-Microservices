package com.raj.user.service.services;

import com.raj.user.service.entities.User;

import java.util.List;

public interface UserService {

    //get single user
    User createUser(User user);

    //get all user
    List<User> getAllUser();

    //get user by id
    User getUser(String userId);

    //delete user by id
    User deleteUser(String userId);

    //update user by id
    User updateUser(User user);


    User promoteUserToAdmin(String userId);

}
