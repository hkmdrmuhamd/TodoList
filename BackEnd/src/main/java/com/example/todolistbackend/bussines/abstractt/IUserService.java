package com.example.todolistbackend.bussines.abstractt;



import com.example.todolistbackend.entities.User;
import com.example.todolistbackend.utilities.DataResult;
import com.example.todolistbackend.utilities.Result;

import java.util.List;

public interface IUserService {

    DataResult<List<User>> getAllUser();

    DataResult<User> getOneUserById(int userId);


    Result createOneUser(User user);


    DataResult<User> getByUserName(String userName);

}

